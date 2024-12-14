package com.example.otchallenge.presenter

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.util.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Presenter class for managing the book list and handling user interactions.
 *
 * @property view The view interface to communicate with the UI.
 * @property repository The repository to fetch book data.
 */
class BookPresenter @Inject constructor(
    private val view: BookContract.View,
    private val repository: BookContract.Repository,
) : BookContract.Presenter, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    /**
     * Loads the list of books and updates the view.
     */
    override fun loadBooksList() {
        launch {
            repository.fetchBookList().collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        withContext(Dispatchers.Main) {
                            view.showLoading()
                        }
                    }

                    is ResultWrapper.Success -> {
                        withContext(Dispatchers.Main) {
                            view.hideLoading()
                            view.showBooksList(books = result.data.results.books)
                        }
                    }

                    is ResultWrapper.Failure -> {
                        withContext(Dispatchers.Main) {
                            view.hideLoading()
                            val errorMessage = when (result.throwable) {
                                is IOException -> NETWORK_ERROR_IO_EXCEPTION
                                else -> result.throwable.message ?: GENERAL_EXCEPTION
                            }
                            view.showError(message = errorMessage)
                        }
                    }
                }
            }
        }
    }

    override fun onNetworkLost() {
        launch {
            withContext(Dispatchers.Main) {
                view.showError(message = NETWORK_CONNECTION_LOST)
            }
        }
    }

    override fun cleanup() {
        job.cancel()
    }

    private companion object {
        const val NETWORK_ERROR_IO_EXCEPTION = "Network Error: Please check your connection"
        const val GENERAL_EXCEPTION = "Error Occurred during Fetch"
        const val NETWORK_CONNECTION_LOST = "Network connection lost"
    }
}