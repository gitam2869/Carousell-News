package com.app.carousell.newsdata.repository

import com.app.carousell.common.DateTimeUtils
import com.app.carousell.common.NetworkResult
import com.app.carousell.newsdata.model.Article
import com.app.carousell.newsdata.model.Articles
import com.app.carousell.newsdata.remote.NewsService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val newsService: NewsService) {

    private val compositeDisposable = CompositeDisposable()

    fun getArticles(callback: (NetworkResult<Articles>) -> Unit) {
        newsService.getArticles()
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
                    callback(NetworkResult.Error(message = errorMessage))
                }

                override fun onSuccess(t: Articles) {
                    callback(NetworkResult.Success(t))
                }
            })

    }

    fun sortArticles(
        articles: Articles,
        comparator: Comparator<Article>,
        callback: (NetworkResult<Articles>) -> Unit
    ) {
        Single.fromCallable {
            return@fromCallable articles.sortedWith(comparator)
        }.delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Articles> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    callback(NetworkResult.Error(message = "Filtering Not Possible"))
                }

                override fun onSuccess(t: Articles) {
                    callback(NetworkResult.Success(t))
                }
            })
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

}