package com.ch4vi.distilled.featureTVShows.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ch4vi.distilled.featureTVShows.domain.model.Page
import com.ch4vi.distilled.featureTVShows.domain.model.TVShow
import com.ch4vi.distilled.featureTVShows.domain.usecase.GetTVShows
import com.ch4vi.distilled.featureTVShows.domain.util.Event
import com.ch4vi.distilled.featureTVShows.domain.util.EventObserver
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
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
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
internal class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    lateinit var getTVShows: GetTVShows

    @RelaxedMockK
    lateinit var observer: EventObserver<HomeViewState>

    private lateinit var sut: HomeViewModel
    private lateinit var captor: MutableList<Event<HomeViewState>>

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        captor = mutableListOf()
        sut = HomeViewModel(getTVShows)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        captor.clear()
    }

    @Test
    fun `GIVEN viewModel WHEN Init success THEN fetch first page`() {
        sut.viewState.observeForever(observer)
        val page = Page(1, 10, listOf())

        coEvery { getTVShows(any()) } returns flowOf(page)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            verify { observer.onChanged(capture(captor)) }

            coVerify { getTVShows(null) }

            assertEquals(2, captor.size)
            assertIs<HomeViewState.Loading>(captor[0].get())
            assertIs<HomeViewState.BindTVShows>(captor[1].get())
        }
    }

    @Test
    fun `GIVEN viewModel WHEN GetNextPage success THEN fetch next page`() {
        sut.viewState.observeForever(observer)
        val page = Page(1, 1, listOf(TVShow(1, "foo", "foo.jpg")))

        coEvery { getTVShows(any()) } returns flowOf(page)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            sut.dispatch(HomeEvent.GetNextPage)

            verify { observer.onChanged(capture(captor)) }

            coVerify {
                getTVShows(null)
                getTVShows(page)
            }

            assertEquals(4, captor.size)
            assertIs<HomeViewState.Loading>(captor[2].get())
            assertIs<HomeViewState.BindTVShows>(captor[3].get())
        }
    }

    @Test
    fun `GIVEN viewModel WHEN GetNextPage empty list THEN stop fetching`() {
        sut.viewState.observeForever(observer)
        val page = Page(1, 1, listOf())

        coEvery { getTVShows(any()) } returns flowOf(page)

        runBlocking {
            sut.dispatch(HomeEvent.Init)
            sut.dispatch(HomeEvent.GetNextPage)

            verify { observer.onChanged(capture(captor)) }

            coVerify(exactly = 1) { getTVShows(any()) }

            assertEquals(2, captor.size)
            assertIs<HomeViewState.Loading>(captor[0].get())
            assertIs<HomeViewState.BindTVShows>(captor[1].get())
        }
    }

    @Test
    fun `GIVEN viewModel WHEN ToggleSorting empty list THEN stop fetching`() {
        sut.viewState.observeForever(observer)
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
            sut.dispatch(HomeEvent.ToggleSorting)

            verify { observer.onChanged(capture(captor)) }

            assertEquals(5, captor.size)

            assertIs<HomeViewState.BindTVShows>(captor[3].get())
            val topRatedSorting = captor[3].forceGet() as HomeViewState.BindTVShows
            assertEquals("C, B, X, D, A, Z", topRatedSorting.items.joinToString { it.title })

            assertIs<HomeViewState.BindTVShows>(captor[4].get())
            val alphabetSorting = captor[4].forceGet() as HomeViewState.BindTVShows
            assertEquals("A, B, C, D, X, Z", alphabetSorting.items.joinToString { it.title })
        }
    }
}
