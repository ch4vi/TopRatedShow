package com.ch4vi.distilled.featureTVShows.data.api.datasource

import com.ch4vi.distilled.featureTVShows.data.api.service.TVShowsService
import com.ch4vi.distilled.featureTVShows.domain.model.Failure
import com.ch4vi.distilled.featureTVShows.domain.model.Page
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.ConnectException
import kotlin.test.BeforeTest
import kotlin.test.assertIs

internal class TVShowApiDataSourceTest {

    @RelaxedMockK
    lateinit var service: TVShowsService

    private val apiConstants by lazy { ApiConstants }
    private lateinit var sut: TVShowApiDataSource

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
        sut = TVShowApiDataSource(service)
    }

    @Test
    fun `GIVEN success response WHEN getRepos THEN returns Page`() {
        coEvery { service.getTopRatedTVShows(any(), any(), any()) } returns apiConstants.pageDTO

        runBlocking {
            val output = sut.fetchTopRatedTVShows(1)

            assertIs<Page>(output)
        }
    }

    @Test(expected = Failure.NetworkFailure::class)
    fun `GIVEN network error WHEN getRepos THEN throws NetworkFailure`() {
        coEvery { service.getTopRatedTVShows(any(), any(), any()) } throws ConnectException()

        runBlocking {
            sut.fetchTopRatedTVShows(0)
        }
    }
}
