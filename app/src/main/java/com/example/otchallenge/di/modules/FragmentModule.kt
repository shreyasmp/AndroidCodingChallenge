package com.example.otchallenge.di.modules

import com.example.otchallenge.view.BookListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeBookListFragment(): BookListFragment
}