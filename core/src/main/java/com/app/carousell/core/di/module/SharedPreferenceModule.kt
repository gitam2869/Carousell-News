package com.app.carousell.core.di.module

import android.content.Context
import android.content.SharedPreferences
import com.app.carousell.core.util.Constant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferenceModule {

    @Singleton
    @Provides
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(
            Constant.APP_SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }
}