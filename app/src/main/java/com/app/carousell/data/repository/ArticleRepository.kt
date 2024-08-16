package com.app.carousell.data.repository

import com.app.carousell.data.model.Article
import com.app.carousell.data.model.Articles
import com.app.carousell.data.remote.ArticleService
import com.app.carousell.util.DateTimeUtils
import com.app.carousell.util.Response
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val articleService: ArticleService) {

    private val compositeDisposable = CompositeDisposable()

    fun getArticles(callback: (Response<Articles>) -> Unit) {
        callback(Response.Loading("Fetching Data"))
        articleService.getArticles()
            .subscribeOn(Schedulers.io())
            .map {
                it.map { article ->
                    article.apply {
                        article.creationDate = DateTimeUtils.getTimeAgoFromSeconds(article.timeCreated)
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Articles> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    val errorMessage = when (e) {
                        is HttpException -> {
                            e.localizedMessage
                        }

                        is UnknownHostException -> {
                            "Network not available!"
                        }

                        else -> {
                            "Something went wrong!"
                        }
                    }
                    callback(Response.Error(message = errorMessage))
                }

                override fun onSuccess(t: Articles) {
                    callback(Response.Success(t))
                }
            })

    }

    fun sortArticles(
        articles: Articles,
        comparator: Comparator<Article>,
        callback: (Response<Articles>) -> Unit
    ) {
        callback(Response.Loading("Filtering Data"))
        Single.fromCallable {
            return@fromCallable articles.sortedWith(comparator)
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Articles> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    callback(Response.Error(message = "Filtering Not Possible"))
                }

                override fun onSuccess(t: Articles) {
                    callback(Response.Success(t))
                }
            })
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

}