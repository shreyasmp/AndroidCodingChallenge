package com.example.otchallenge.presenter

import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.models.Book
import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.util.provideBookResponse
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class BookPresenterTest {

    private lateinit var mockView: BookContract.View
    private lateinit var mockRepository: BookContract.Repository
    private lateinit var bookPresenter: BookPresenter

    @Before
    fun setUp() {
        mockView = mockk(relaxed = true)
        mockRepository = mockk()
        bookPresenter = BookPresenter(mockView, mockRepository)

        every { mockRepository.fetchBookList(any()) } just Runs
    }

    @Test
    fun `loadBooksList should show loading spinner and call repository`() {
        // Act
        bookPresenter.loadBooksList()

        // Verify that the loading spinner is shown
        verify { mockView.showLoading() }

        // Verify that the repository method is called to fetch the book list
        verify { mockRepository.fetchBookList(bookPresenter) }
    }

    @Test
    fun `onSuccess should show books list when response is successful`() {
        // Arrange
        val bookResponse = provideBookResponse()
        val successResult: Result<BookResponse> = Result.success(bookResponse)

        // Simulate success callback
        bookPresenter.onSuccess(successResult)

        // Verify that the loading spinner is hidden
        verify { mockView.hideLoading() }

        // Verify that the books list is shown
        val capturedBooks = slot<List<Book>>()
        verify { mockView.showBooksList(capture(capturedBooks)) }

        // Retrieve the captured books list
        val capturedBooksList = capturedBooks.captured

        // Verify that the correct list is passed to the view

        assertEquals(2, capturedBooksList.size)
        assertEquals("book1", capturedBooksList[0].title)
        assertEquals("description2", capturedBooksList[1].description)
    }

    @Test
    fun `onSuccess should show error message when response is a failure`() {
        // Arrange
        val errorResult: Result<BookResponse> = Result.failure(IOException("Network error"))

        // Act
        bookPresenter.onSuccess(errorResult)

        // Verify that the loading spinner is hidden
        verify { mockView.hideLoading() }

        // Verify that the correct error message is shown
        verify { mockView.showError("Network Error: Please check your connection") }
    }

    @Test
    fun `onSuccess should show generic error message on unexpected failure`() {
        // Arrange
        val errorResult: Result<BookResponse> =
            Result.failure(Exception("Error Occurred during Fetch"))

        // Act
        bookPresenter.onSuccess(errorResult)

        // Verify that the loading spinner is hidden
        verify { mockView.hideLoading() }

        // Verify that the generic error message is shown
        verify { mockView.showError("Error Occurred during Fetch") }
    }

    @Test
    fun `loadBooksList should hide loading spinner when data is loaded or error occurs`() {
        // Arrange
        val errorResult: Result<BookResponse> = Result.failure(IOException("Network error"))

        // Act
        bookPresenter.loadBooksList()

        // Act again for the failure
        bookPresenter.onSuccess(errorResult)

        // Verify that the loading spinner is hidden
        verify { mockView.hideLoading() }
    }

    @Test
    fun `onNetworkLost should show network lost error message`() {
        // Act
        bookPresenter.onNetworkLost()

        // Verify that the network lost error message is shown
        verify { mockView.showError("Network connection lost") }
    }
}