package com.example.otchallenge.view

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.otchallenge.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookListFragmentTest {

    private var scenario: FragmentScenario<BookListFragment>? = null

    @Before
    fun setUp() {
        scenario = FragmentScenario.launchInContainer(BookListFragment::class.java)
    }

    @After
    fun tearDown() {
        scenario?.close()
    }

    @Test
    fun testBooksListIsDisplayed() {
        onView(withId(R.id.books_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testProgressIndicatorIsDisplayed() {
        onView(withId(R.id.progress_indicator))
            .check(matches(isDisplayed()))
    }
}