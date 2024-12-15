package com.example.otchallenge

import android.app.Application
import com.example.otchallenge.di.AppComponent
import com.example.otchallenge.di.DaggerAppComponent
import com.example.otchallenge.di.modules.AppModule
import com.example.otchallenge.di.modules.PresenterModule
import com.example.otchallenge.view.BookListFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MyApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = activityInjector

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .presenterModule(PresenterModule(BookListFragment()))
            .build()
        appComponent.inject(this)
    }
}
