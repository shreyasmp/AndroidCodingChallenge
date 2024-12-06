package com.example.otchallenge.contract

import androidx.annotation.UiThread
import com.example.otchallenge.models.Book
import com.example.otchallenge.models.BookResponse

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
         */
        fun loadBooksList()

        /**
         * Handles the event when the network is lost.
         */
        fun onNetworkLost()
    }

    /**
     * Interface representing the Repository in the MVP pattern.
     * It is responsible for fetching book data from the data source.
     */
    interface Repository {
        /**
         * Fetches the list of books from the data source and returns the result via a callback.
         *
         * @param listener The listener to return the result of the data fetch operation.
         *                 If the fetch operation is successful, the listener's onSuccess method is called with the result.
         *                 If the fetch operation fails, the listener's onSuccess method is called with an exception.
         */
        fun fetchBookList(listener: OnFinishedListener)

        interface OnFinishedListener {
            /**
             * Called when the book data fetch operation is finished.
             *
             * @param response The result of the fetch operation, containing either the fetched data or an exception.
             */
            fun onSuccess(response: Result<BookResponse>?)
        }
    }
}