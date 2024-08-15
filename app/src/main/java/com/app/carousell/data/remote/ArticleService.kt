package com.app.carousell.data.remote

import com.app.carousell.data.model.Articles
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ArticleService {

    @GET("carousell-interview-assets/android/carousell_news.json")
    fun getArticles(): Single<Articles>
}