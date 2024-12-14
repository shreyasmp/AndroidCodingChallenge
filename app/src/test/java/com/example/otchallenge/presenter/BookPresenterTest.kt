package com.example.otchallenge.presenter

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.util.ResultWrapper
import com.example.otchallenge.util.provideBookResponse
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class BookPresenterTest {

    private lateinit var mockView: BookContract.View
    private lateinit var mockRepository: BookContract.Repository
    private lateinit var bookPresenter: BookPresenter
    private val testDispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        mockView = mockk(relaxed = true)
        mockRepository = mockk()
        bookPresenter = BookPresenter(mockView, mockRepository)
        Dispatchers.setMain(testDispatcher)
        clearMocks(mockView, mockRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks() // Add this line to reset mocks
        Dispatchers.resetMain()
    }

    @Test
    fun `loadBooksList should show loading spinner and call repository`() = runBlocking {
        // Arrange
        val bookResponse = provideBookResponse()
        coEvery { mockRepository.fetchBookList() } returns flow {
            emit(ResultWrapper.Loading(true))
            emit(ResultWrapper.Success(bookResponse))
        }

        // Act
        bookPresenter.loadBooksList()

        // Assert
        coVerifySequence {
            mockView.showLoading()
            mockView.hideLoading()
            mockView.showBooksList(bookResponse.results.books)
        }
    }

//    @Test
//    fun `loadBooksList should show error message on failure`() = runBlocking {
//        // Arrange
//        val exception = IOException("Network error")
//        coEvery { mockRepository.fetchBookList() } returns flow {
//            emit(ResultWrapper.Loading(true))
//            emit(ResultWrapper.Failure(exception))
//            emit(ResultWrapper.Loading(false))
//        }
//
//        // Act
//        bookPresenter.loadBooksList()
//
//        // Assert
//        coVerifySequence {
//            mockView.showLoading()
//            mockView.showError("Network Error: Please check your connection")
//            mockView.hideLoading()
//        }
//    }

    @Test
    fun `onNetworkLost should show network lost error message`() = runBlocking {
        // Act
        bookPresenter.onNetworkLost()

        // Assert
        coVerify { mockView.showError("Network connection lost") }
    }
}