package com.app.carousell.newsdata.repository

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.Supplier
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement;
import java.util.concurrent.Callable


class RxJavaSchedulersRule : TestRule {
    private val immediate = Schedulers.trampoline()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { scheduler: Supplier<Scheduler?>? -> immediate }
                RxJavaPlugins.setIoSchedulerHandler { scheduler: Scheduler? -> immediate }
                RxJavaPlugins.setInitComputationSchedulerHandler { scheduler: Supplier<Scheduler?>? -> immediate }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler: Scheduler? -> immediate }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler: Supplier<Scheduler?>? -> immediate }
                RxJavaPlugins.setNewThreadSchedulerHandler { scheduler: Scheduler? -> immediate }
                RxJavaPlugins.setInitSingleSchedulerHandler { scheduler: Supplier<Scheduler?>? -> immediate }
                RxJavaPlugins.setSingleSchedulerHandler { scheduler: Scheduler? -> immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> immediate }
                RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler: Scheduler? -> immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}