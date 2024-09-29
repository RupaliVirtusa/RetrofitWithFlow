package com.assignment.codingassignment

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.assignment.codingassignment.di.ApplicationTestModule
import com.assignment.codingassignment.presentation.MainActivity
import com.assignment.codingassignment.util.FileReader
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(ApplicationTestModule::class)
@HiltAndroidTest
class RecipeActivityTestMockServer {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.getHeader("Authorization")!! == BuildConfig.APP_TOKEN) {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("recipe.json"))
            }

            return MockResponse().setResponseCode(404)
        }
    }

    @Test
    fun test_happyPath_reipeListed() {
        mockWebServer.dispatcher = dispatcher
        mActivityRule.launchActivity(null)

        Thread.sleep(3000)
        Espresso.onView(withId(R.id.tvRecipeCount))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.rvRecipe))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun test_responseFailure_failureMessageIsDisplayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(404)
            }
        }
        mActivityRule.launchActivity(null)
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.rvRecipe))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(withId(R.id.tvRecipeCount))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))

    }

}