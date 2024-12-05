package com.example.otchallenge.di

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.presenter.BookPresenter
import com.example.otchallenge.repository.BookRepository
import com.example.otchallenge.repository.BookRepositoryImpl
import com.example.otchallenge.service.BookApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresenterModule(private val view: BookContract.View) {

    @Provides
    @Singleton
    fun provideView(): BookContract.View = view

    @Provides
    @Singleton
    fun provideBooksRepository(apiService: BookApiService): BookRepository {
        return BookRepositoryImpl(apiService = apiService)
    }

    @Provides
    @Singleton
    fun provideBookPresenter(view: BookContract.View, repository: BookRepository): BookPresenter {
        return BookPresenter(view = view, repository = repository)
    }
}