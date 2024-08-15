package com.app.carousell.data.repository

import com.app.carousell.data.model.Articles
import com.app.carousell.data.remote.ArticleService
import com.app.carousell.ui.ApiResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val articleService: ArticleService) {

    private val compositeDisposable = CompositeDisposable()

    fun getArticles(callback: (ApiResponse<Articles>) -> Unit) {
        callback(ApiResponse.Loading())
        articleService.getArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Articles> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    val errorMessage = when (e) {
                        is HttpException -> {
                            e.message()
                        }

                        is UnknownHostException -> {
                            "Network not available!"
                        }

                        else -> {
                            "Something went wrong!"
                        }
                    }
                    callback(ApiResponse.Error(message = errorMessage))
                }

                override fun onSuccess(t: Articles) {
                    callback(ApiResponse.Success(t))
                }
            })

    }

    fun dispose() {
        compositeDisposable.dispose()
    }

}