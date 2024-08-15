package com.app.carousell.di

import com.app.carousell.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
}