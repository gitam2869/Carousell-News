package com.app.carousell.core.di.component

import android.content.Context
import android.content.SharedPreferences
import com.app.carousell.core.di.module.CoreModule
import com.app.carousell.core.di.module.NetworkModule
import com.app.carousell.core.di.module.SharedPreferenceModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class, SharedPreferenceModule::class, NetworkModule::class])
interface CoreComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    fun sharedPreferences(): SharedPreferences
    fun retrofit() : Retrofit
}