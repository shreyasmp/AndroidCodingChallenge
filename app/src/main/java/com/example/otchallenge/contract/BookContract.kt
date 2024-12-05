package com.example.otchallenge.contract

import androidx.annotation.UiThread
import com.example.otchallenge.models.Book

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
}