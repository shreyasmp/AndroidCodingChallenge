package com.example.otchallenge.repository

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.service.BookApiService
import com.example.otchallenge.util.provideBookResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepositoryImplTest {

    private lateinit var apiService: BookApiService
    private lateinit var repository: BookRepositoryImpl
    private lateinit var listener: BookContract.Repository.OnFinishedListener

    @Before
    fun setUp() {
        apiService = mockk()
        listener = mockk(relaxed = true)
        repository = BookRepositoryImpl(apiService)
    }

    @Test
    fun `fetchBookList should call onSuccess with data when response is successful`() {
        // Arrange
        val call = mockk<Call<BookResponse>>()
        val response = mockk<Response<BookResponse>>()
        val bookResponse = provideBookResponse()

        every { apiService.getBooks() } returns call
        every { call.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<BookResponse>
            callback.onResponse(call, response)
        }
        every { response.isSuccessful } returns true
        every { response.body() } returns bookResponse

        // Act
        repository.fetchBookList(listener)

        // Assert
        verify { listener.onSuccess(Result.success(bookResponse)) }
    }

    @Test
    fun `fetchBookList should call onSuccess with exception when response is not successful`() {
        // Arrange
        val call = mockk<Call<BookResponse>>()
        val response = mockk<Response<BookResponse>>()

        every { apiService.getBooks() } returns call
        every { call.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<BookResponse>
            callback.onResponse(call, response)
        }
        every { response.isSuccessful } returns false

        // Act
        repository.fetchBookList(listener)

        // Assert
        verify { listener.onSuccess(match { it.isFailure }) }
    }

    @Test
    fun `fetchBookList should call onSuccess with exception when response body is null`() {
        // Arrange
        val call = mockk<Call<BookResponse>>()
        val response = mockk<Response<BookResponse>>()

        every { apiService.getBooks() } returns call
        every { call.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<BookResponse>
            callback.onResponse(call, response)
        }
        every { response.isSuccessful } returns true
        every { response.body() } returns null

        // Act
        repository.fetchBookList(listener)

        // Assert
        verify { listener.onSuccess(match { it.isFailure }) }
    }

    @Test
    fun `fetchBookList should call onSuccess with exception when onFailure is called`() {
        // Arrange
        val call = mockk<Call<BookResponse>>()
        val throwable = Throwable("Network error")

        every { apiService.getBooks() } returns call
        every { call.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<BookResponse>
            callback.onFailure(call, throwable)
        }

        // Act
        repository.fetchBookList(listener)

        // Assert
        verify { listener.onSuccess(match { it.isFailure }) }
    }
}