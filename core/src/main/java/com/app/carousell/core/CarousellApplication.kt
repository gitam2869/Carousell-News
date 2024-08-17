package com.app.carousell.core

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.app.carousell.core.di.CoreComponent
import com.app.carousell.core.di.DaggerCoreComponent

class CarousellApplication : Application() {

    val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) =
            (context.applicationContext as CarousellApplication).coreComponent
    }
}

fun Activity.coreComponent() = CarousellApplication.coreComponent(this)
fun Fragment.coreComponent() = CarousellApplication.coreComponent(requireContext())