package com.example.otchallenge.di

import com.example.otchallenge.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        PresenterModule::class]
)
interface AppComponent {
    fun inject(activity: MainActivity)
}
