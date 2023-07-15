package com.ch4vi.distilled.featureTVShows.presentation.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4vi.distilled.featureTVShows.domain.model.Failure
import com.ch4vi.distilled.featureTVShows.domain.model.Page
import com.ch4vi.distilled.featureTVShows.domain.model.TVShow
import com.ch4vi.distilled.featureTVShows.domain.usecase.GetTVShows
import com.ch4vi.distilled.featureTVShows.domain.util.Event
import com.ch4vi.distilled.featureTVShows.domain.util.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeEvent {
    object Init : HomeEvent()
    object GetNextPage : HomeEvent()
    object ToggleSorting : HomeEvent()
}

sealed class HomeViewState {
    data class BindTVShows(val items: List<TVShow>) : HomeViewState()
    object Loading : HomeViewState()
    data class NetworkError(val message: String?) : HomeViewState()
    object GenericError : HomeViewState()
}

enum class SortType {
    TopRated, Alphabetic
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTVShows: GetTVShows
) : ViewModel() {

    private val mutableViewState = MutableLiveData<Event<HomeViewState>>()
    val viewState: LiveData<Event<HomeViewState>>
        get() = mutableViewState

    private val pagedItems: MutableMap<Int, List<TVShow>> = mutableMapOf()
    private var lastPage: Page? = null
    private var sorting: SortType = SortType.TopRated

    fun dispatch(event: HomeEvent) {
        when (event) {
            is HomeEvent.Init -> getShows(refresh = true)
            is HomeEvent.GetNextPage -> getShows(refresh = false)
            HomeEvent.ToggleSorting -> toggleSorting()
        }
    }

    private fun getShows(refresh: Boolean) {
        if (refresh) {
            lastPage = null
        } else if (lastPage?.items?.isEmpty() == true) {
            return
        }

        changeViewState(HomeViewState.Loading)
        viewModelScope.launch {
            getTVShows(lastPage)
                .catch { onError(it) }
                .collect { page ->
                    lastPage = page
                    pagedItems[page.pageIndex] = page.items
                    changeViewState(HomeViewState.BindTVShows(getSortedShows()))
                }
        }
    }

    private fun toggleSorting() {
        sorting = when (sorting) {
            SortType.TopRated -> SortType.Alphabetic
            SortType.Alphabetic -> SortType.TopRated
        }

        changeViewState(HomeViewState.BindTVShows(getSortedShows()))
    }

    private fun getSortedShows(): List<TVShow> =
        when (sorting) {
            SortType.TopRated -> pagedItems.toSortedMap().values.flatten()
            SortType.Alphabetic -> pagedItems.toSortedMap().values.flatten().sortedBy { it.title }
        }

    private fun changeViewState(viewState: HomeViewState) =
        mutableViewState.postValue(viewState.toEvent())

    @VisibleForTesting
    internal fun onError(error: Throwable) {
        when (error) {
            is Failure.NetworkFailure -> changeViewState(HomeViewState.NetworkError(error.message))
            else -> changeViewState(HomeViewState.GenericError)
        }
    }
}
