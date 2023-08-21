package com.ch4vi.topRatedShows.featureTVShows.data.repository

import com.ch4vi.topRatedShows.featureTVShows.data.api.datasource.TVShowApiDataSource
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Failure
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.repository.TVShowRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

internal class TVShowRepositoryImplTest {

    @RelaxedMockK
    lateinit var dataSource: TVShowApiDataSource

    private lateinit var sut: TVShowRepository

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
        sut = TVShowRepositoryImpl(dataSource)
    }

    @Test
    fun `GIVEN repository WHEN getTopRatedTVShows success THEN return page`() {
        val page = Page(1, 10, listOf())

        coEvery { dataSource.fetchTopRatedTVShows(any()) } returns page

        runBlocking {
            val output = sut.getTopRatedTVShows(1)

            assertIs<Page>(output)
        }
    }

    @Test(expected = Failure.MapperFailure::class)
    fun `GIVEN repository WHEN getTopRatedTVShows error THEN propagates error`() {
        coEvery { dataSource.fetchTopRatedTVShows(any()) } throws Failure.MapperFailure

        runBlocking {
            sut.getTopRatedTVShows(1)
        }
    }
}
