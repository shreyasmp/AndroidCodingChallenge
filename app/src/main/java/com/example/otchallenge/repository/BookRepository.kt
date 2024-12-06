package com.example.otchallenge.repository

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.service.BookApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


/**
 * This class is responsible for fetching book data from the API.
 *
 * @property apiService The API service used to fetch book data.
 */
open class BookRepositoryImpl @Inject constructor(
    private val apiService: BookApiService,
) : BookContract.Repository {

    /**
     * Fetches the list of books from the API and returns the result via a callback.
     *
     * @param listener The listener to return the result of the API call.
     *                 If the API call is successful and the response body is not null,
     *                 the listener's onSuccess method is called with the result.
     *                 If the response body is null or the API call fails,
     *                 the listener's onSuccess method is called with an exception.
     *                 Using Dependency Inversion principle here to enabled Presenter testing.
     */
    override fun fetchBookList(listener: BookContract.Repository.OnFinishedListener) {
        apiService.getBooks().enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listener.onSuccess(Result.success(it))
                    } ?: listener.onSuccess(Result.failure(Exception(NO_DATA_AVAILABLE)))
                } else {
                    listener.onSuccess(Result.failure(Exception(FAILED_LOAD_STATE)))
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                listener.onSuccess(Result.failure(t))
            }
        })
    }

    private companion object {
        const val NO_DATA_AVAILABLE = "No data available"
        const val FAILED_LOAD_STATE = "Failed to load data"
    }
}