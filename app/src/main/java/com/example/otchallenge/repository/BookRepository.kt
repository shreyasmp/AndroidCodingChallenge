package com.example.otchallenge.repository

import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.service.BookApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

interface BookRepository {
    fun fetchBookList(callback: (Result<BookResponse>) -> Unit)
}

/**
 * Implementation of the BookRepository interface.
 * This class is responsible for fetching book data from the API.
 *
 * @property apiService The API service used to fetch book data.
 */
class BookRepositoryImpl @Inject constructor(
    private val apiService: BookApiService,
) : BookRepository {

    /**
     * Fetches the list of books from the API and returns the result via a callback.
     *
     * @param callback The callback to return the result of the API call.
     */
    override fun fetchBookList(callback: (Result<BookResponse>) -> Unit) {
        apiService.getBooks().enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Exception(NO_DATA_AVAILABLE)))
                } else {
                    callback(Result.failure(Exception(FAILED_LOAD_STATE)))
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    private companion object {
        const val NO_DATA_AVAILABLE = "No data available"
        const val FAILED_LOAD_STATE = "Failed to load data"
    }
}