package com.ch4vi.topRatedShows.featureTVShows.domain.usecase

import com.ch4vi.topRatedShows.common.constants.Constants.START_PAGE
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.model.TVShow
import com.ch4vi.topRatedShows.featureTVShows.domain.repository.TVShowRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetTVShowsTest {

    @RelaxedMockK
    lateinit var repository: TVShowRepository

    private lateinit var sut: GetTVShows

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
        sut = GetTVShows(repository)
    }

    @Test
    fun `GIVEN empty page WHEN invoke sut success THEN getTopRatedTVShows is called with start page`() {
        val page = Page(1, 10, listOf())

        coEvery { repository.getTopRatedTVShows(any()) } returns page

        runBlocking {
            val output = sut(null).first()

            coVerify { repository.getTopRatedTVShows(START_PAGE) }

            assertIs<Page>(output)
        }
    }

    @Test
    fun `GIVEN last page WHEN invoke sut success THEN getTopRatedTVShows return empty items`() {
        val page = Page(1, 1, listOf(TVShow(1, "foo", "foo.jpg")))

        coEvery { repository.getTopRatedTVShows(any()) } returns page

        runBlocking {
            val output = sut(page).first()

            coVerify(exactly = 0) { repository.getTopRatedTVShows(any()) }

            assertEquals(0, output.items.size)
        }
    }

    @Test
    fun `GIVEN middle page WHEN invoke sut success THEN getTopRatedTVShows return next page`() {
        val page = Page(1, 10, listOf(TVShow(1, "foo", "foo.jpg")))

        coEvery { repository.getTopRatedTVShows(any()) } returns page

        runBlocking {
            val output = sut(page).first()

            coVerify { repository.getTopRatedTVShows(2) }

            assertEquals(1, output.items.size)
        }
    }
}
