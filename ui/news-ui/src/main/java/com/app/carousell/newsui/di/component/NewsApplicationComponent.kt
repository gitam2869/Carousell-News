package com.app.carousell.newsui.di.component

import com.app.carousell.common.scopes.Fragment
import com.app.carousell.core.di.component.CoreComponent
import com.app.carousell.newsdata.di.module.NewsDataModule
import com.app.carousell.newsui.presentation.fragment.ArticlesFragment
import dagger.Component

@Fragment
@Component(dependencies = [CoreComponent::class], modules = [NewsDataModule::class])
interface NewsApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): NewsApplicationComponent
    }

    fun inject(articlesFragment: ArticlesFragment)

}