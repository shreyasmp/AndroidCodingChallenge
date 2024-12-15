package com.example.otchallenge.di

import android.app.Application
import com.example.otchallenge.MainActivity
import com.example.otchallenge.MyApplication
import com.example.otchallenge.di.modules.AppModule
import com.example.otchallenge.di.modules.FragmentModule
import com.example.otchallenge.di.modules.NetworkModule
import com.example.otchallenge.di.modules.PresenterModule
import com.example.otchallenge.view.BookListFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        FragmentModule::class,
        NetworkModule::class,
        PresenterModule::class]
)
interface AppComponent : AndroidInjector<MyApplication> {

    fun inject(activity: MainActivity)
    fun inject(fragment: BookListFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun appModule(appModule: AppModule): Builder
        fun presenterModule(presenterModule: PresenterModule): Builder

        fun build(): AppComponent
    }
}
