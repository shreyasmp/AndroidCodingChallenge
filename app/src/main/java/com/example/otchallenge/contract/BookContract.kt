package com.example.otchallenge.contract

import androidx.annotation.UiThread
import com.example.otchallenge.models.Book
import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.util.ResultWrapper
import kotlinx.coroutines.flow.Flow

/**
 * Contract interface for the Book feature, defining the View and Presenter interfaces.
 */
interface BookContract {

    /**
     * Interface representing the View in the MVP pattern.
     * It should be implemented by the Activity or Fragment.
     */
    @UiThread
    interface View {
        /**
         * Displays a list of books.
         *
         * @param books The list of books to display.
         */
        fun showBooksList(books: List<Book>)

        /**
         * Displays an error message.
         *
         * @param message The error message to display.
         */
        fun showError(message: String)

        /**
         * Shows a loading indicator.
         */
        fun showLoading()

        /**
         * Hides the loading indicator.
         */
        fun hideLoading()
    }

    /**
     * Interface representing the Presenter in the MVP pattern.
     * It should be implemented by the Presenter class.
     */
    interface Presenter {
        /**
         * Loads the list of books.
         * This method is responsible for fetching the book data from the repository
         * and updating the view with the retrieved data.
         */
        fun loadBooksList()

        /**
         * Handles the event when the network is lost.
         * This method is called to notify the view about the network disconnection
         * and to take appropriate actions, such as displaying an error message.
         */
        fun onNetworkLost()

        /**
         * Cleans up resources used by the presenter.
         * This method is called to release any resources or cancel any ongoing operations
         * when the presenter is no longer needed.
         */
        fun cleanup()
    }

    /**
     * Interface representing the Repository in the MVP pattern.
     * It is responsible for fetching book data from the data source.
     */
    interface Repository {
        /**
         * Fetches the list of books from the data source and returns the result.
         * This method is a suspend function that performs the network or database operation
         * to retrieve the book list and wraps the result in a Flow of ResultWrapper.
         *
         * @return A Flow emitting ResultWrapper containing the book list or an error.
         */
        suspend fun fetchBookList(): Flow<ResultWrapper<BookResponse>>
    }
}