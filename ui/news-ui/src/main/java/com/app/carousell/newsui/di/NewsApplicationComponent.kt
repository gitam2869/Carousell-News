package com.app.carousell.newsui.di

import com.app.carousell.common.scopes.Fragment
import com.app.carousell.core.di.CoreComponent
import com.app.carousell.newsdata.di.NewsDataModule
import com.app.carousell.newsui.CarousellNewsFragment
import dagger.Component

@Fragment
@Component(dependencies = [CoreComponent::class], modules = [NewsDataModule::class])
interface NewsApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): NewsApplicationComponent
    }

    fun inject(carousellNewsFragment: CarousellNewsFragment)

}