package com.example.otchallenge.repository

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.service.BookApiService
import com.example.otchallenge.util.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * This class is responsible for fetching book data from the API.
 *
 * @property apiService The API service used to fetch book data.
 */
class BookRepositoryImpl @Inject constructor(
    private val apiService: BookApiService,
) : BookContract.Repository {

    /**
     * Fetches the list of books from the API and returns the result.
     * This method is a suspend function that performs the network operation
     * to retrieve the book list and wraps the result in a Flow of ResultWrapper.
     *
     * @return A Flow emitting ResultWrapper containing the book list or an error.
     */
    override suspend fun fetchBookList(): Flow<ResultWrapper<BookResponse>> = flow {
        // Emit a loading state to indicate the operation has started
        emit(ResultWrapper.Loading(isLoading = true))

        // Perform the network request to fetch the book list
        val bookResponse = apiService.getBooks().await()

        // Emit the success state with the retrieved book data
        emit(ResultWrapper.Success(data = bookResponse))
    }.catch { throwable ->
        // Emit the failure state with the encountered throwable
        emit(ResultWrapper.Failure(throwable = throwable))
    }.flowOn(Dispatchers.IO) // Perform the operation on the IO dispatcher
}