package com.example.otchallenge

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testActivityLaunch() {
        scenario.onActivity { activity ->
            assert(activity != null)
        }
    }

    @Test
    fun testIntentData() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("EXTRA_DATA", "Test Data")
        }
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity ->
            val data = activity.intent.getStringExtra("EXTRA_DATA")
            assert(data == "Test Data")
        }
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