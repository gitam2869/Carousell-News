package com.app.carousell.newsdata.di

import com.app.carousell.newsdata.remote.NewsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NewsDataModule {

    @Provides
    fun provideNewsService(retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }
}