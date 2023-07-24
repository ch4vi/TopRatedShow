package com.ch4vi.distilled.featureTVShows.presentation.home

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertListItemCount
import com.ch4vi.distilled.R
import com.ch4vi.distilled.common.di.APIModule
import com.ch4vi.distilled.featureTVShows.presentation.main.MainActivity
import com.ch4vi.distilled.utils.getJsonContent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(APIModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(getJsonContent("tv_shows_list.json"))
            }
        }
        server.start(8080)
    }

    @After
    fun cleanup() {
        scenario.close()
        server.shutdown()
    }

    @Test
    fun homeListIsNotEmpty() {
        scenario = launchActivity()

        assertListItemCount(R.id.tvShowsList, 5)
        assertDisplayedAtPosition(
            listId = R.id.tvShowsList,
            position = 0,
            targetViewId = R.id.tvShowTitle,
            text = "Breaking Bad 2"
        )
    }
}
