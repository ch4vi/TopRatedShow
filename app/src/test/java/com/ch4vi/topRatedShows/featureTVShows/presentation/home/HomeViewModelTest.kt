package com.ch4vi.topRatedShows.featureTVShows.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.model.SortType
import com.ch4vi.topRatedShows.featureTVShows.domain.model.TVShow
import com.ch4vi.topRatedShows.featureTVShows.domain.usecase.GetTVShows
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    lateinit var getTVShows: GetTVShows

    private lateinit var sut: HomeViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        sut = HomeViewModel(getTVShows)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN viewModel WHEN Init success THEN fetch first page`() {
        val item = TVShow(1, "foo", "foo.jpg")
        val expectedState = HomeState(
            list = listOf(item),
            isLoading = false,
            sortType = SortType.TopRated
        )

        val page = Page(1, 10, listOf(item))
        coEvery { getTVShows(any()) } returns flowOf(page)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            val currentState = sut.state.value

            coVerify { getTVShows(null) }

            assertEquals(expectedState, currentState)
        }
    }

    @Test
    fun `GIVEN viewModel WHEN GetNextPage success THEN fetch next page`() {
        val item = TVShow(1, "foo", "foo.jpg")
        val expectedState = HomeState(
            list = listOf(item),
            isLoading = false,
            sortType = SortType.TopRated
        )

        val page = Page(1, 10, listOf(item))
        coEvery { getTVShows(any()) } returns flowOf(page)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            sut.dispatch(HomeEvent.GetNextPage)
            val currentState = sut.state.value

            coVerify {
                getTVShows(null)
                getTVShows(page)
            }

            assertEquals(expectedState, currentState)
        }
    }

    @Test
    fun `GIVEN viewModel WHEN GetNextPage empty list THEN stop fetching`() {
        val expectedState = HomeState(
            list = listOf(),
            isLoading = false,
            sortType = SortType.TopRated
        )

        val page = Page(1, 10, listOf())
        coEvery { getTVShows(null) } returns flowOf(page)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            sut.dispatch(HomeEvent.GetNextPage)
            val currentState = sut.state.value

            coVerify(exactly = 0) { getTVShows(page) }

            assertEquals(expectedState, currentState)
        }
    }

    @Test
    fun `GIVEN viewModel WHEN ToggleSorting empty list THEN stop fetching`() {
        val page = Page(
            1,
            2,
            listOf(
                TVShow(1, "C", ""),
                TVShow(2, "B", ""),
                TVShow(3, "X", "")
            )
        )
        val otherPage = Page(
            2,
            2,
            listOf(
                TVShow(4, "D", ""),
                TVShow(5, "A", ""),
                TVShow(6, "Z", "")
            )
        )

        coEvery { getTVShows(null) } returns flowOf(page)
        coEvery { getTVShows(page) } returns flowOf(otherPage)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            sut.dispatch(HomeEvent.GetNextPage)
            val prevState = sut.state.value

            sut.dispatch(HomeEvent.ToggleSorting)
            val currentState = sut.state.value

            coVerify {
                getTVShows(null)
                getTVShows(page)
            }

            assertEquals(SortType.TopRated, prevState.sortType)
            assertEquals("C, B, X, D, A, Z", prevState.list.joinToString { it.title })

            assertEquals(SortType.Alphabetic, currentState.sortType)
            assertEquals("A, B, C, D, X, Z", currentState.list.joinToString { it.title })
        }
    }
}
