package com.example.otchallenge.repository

import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.service.BookApiService
import com.example.otchallenge.util.ResultWrapper
import com.example.otchallenge.util.provideBookResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BookRepositoryImplTest {

    private lateinit var apiService: BookApiService
    private lateinit var repository: BookRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = BookRepositoryImpl(apiService)
    }

    @Test
    fun `fetchBookList should emit loading, success and data when response is successful`() =
        runBlocking {
            // Arrange
            val bookResponse = provideBookResponse()
            val deferred = mockk<Deferred<BookResponse>>()
            coEvery { deferred.await() } returns bookResponse
            coEvery { apiService.getBooks() } returns deferred

            // Act
            val result = mutableListOf<ResultWrapper<BookResponse>>()
            repository.fetchBookList().collect { result.add(it) }

            // Assert
            assertEquals(2, result.size)
            assertEquals(ResultWrapper.Loading<BookResponse>(true), result[0])
            assertEquals(ResultWrapper.Success(bookResponse), result[1])
        }

    @Test
    fun `fetchBookList should emit loading and failure when response is unsuccessful`() =
        runBlocking {
            // Arrange
            val exception = Exception("Network error")
            val deferred = mockk<Deferred<BookResponse>>()
            coEvery { deferred.await() } throws exception
            coEvery { apiService.getBooks() } returns deferred

            // Act
            val result = mutableListOf<ResultWrapper<BookResponse>>()
            repository.fetchBookList().collect { result.add(it) }

            // Assert
            assertEquals(2, result.size)
            assertEquals(ResultWrapper.Loading<BookResponse>(true), result[0])
            assertEquals(ResultWrapper.Failure<BookResponse>(exception), result[1])
        }
}