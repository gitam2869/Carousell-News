package com.app.carousell

import android.app.Application
import com.app.carousell.di.ApplicationComponent
import com.app.carousell.di.DaggerApplicationComponent

class CarousellApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().build()
    }

}