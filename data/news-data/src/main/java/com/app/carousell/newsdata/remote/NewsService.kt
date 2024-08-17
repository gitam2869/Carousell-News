package com.app.carousell.newsdata.remote

import com.app.carousell.newsdata.model.Articles
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface NewsService {

    @GET("carousell-interview-assets/android/carousell_news.json")
    fun getArticles(): Single<Articles>
}