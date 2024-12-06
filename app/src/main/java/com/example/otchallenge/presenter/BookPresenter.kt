package com.example.otchallenge.presenter

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.models.BookResponse
import java.io.IOException
import javax.inject.Inject

/**
 * Presenter class for managing the book list and handling user interactions.
 *
 * @property view The view interface to communicate with the UI.
 * @property repository The repository to fetch book data.
 */
class BookPresenter @Inject constructor(
    private val view: BookContract.View,
    private val repository: BookContract.Repository,
) : BookContract.Presenter, BookContract.Repository.OnFinishedListener {

    /**
     * Loads the list of books and updates the view.
     */
    override fun loadBooksList() {
        view.showLoading()

        repository.fetchBookList(this)
    }

    override fun onSuccess(response: Result<BookResponse>?) {
        response?.onSuccess { bookResponse ->
            view.hideLoading()
            view.showBooksList(books = bookResponse.results.books)
        }?.onFailure { throwable ->
            val errorMessage = when (throwable) {
                is IOException -> NETWORK_ERROR_IO_EXCEPTION
                else -> throwable.message ?: GENERAL_EXCEPTION
            }
            view.hideLoading()
            view.showError(message = errorMessage)
        }
    }

    /**
     * Handles the event when the network is lost and updates the view.
     */
    override fun onNetworkLost() {
        view.showError(message = NETWORK_CONNECTION_LOST)
    }

    private companion object {
        const val NETWORK_ERROR_IO_EXCEPTION = "Network Error: Please check your connection"
        const val GENERAL_EXCEPTION = "Error Occurred during Fetch"
        const val NETWORK_CONNECTION_LOST = "Network connection lost"
    }
}