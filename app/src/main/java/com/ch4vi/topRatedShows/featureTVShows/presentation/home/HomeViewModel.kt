package com.ch4vi.topRatedShows.featureTVShows.presentation.home

import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4vi.topRatedShows.R
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Failure
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.model.SortType
import com.ch4vi.topRatedShows.featureTVShows.domain.model.TVShow
import com.ch4vi.topRatedShows.featureTVShows.domain.usecase.GetTVShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class HomeState(
    val list: List<TVShow> = emptyList(),
    val sortType: SortType = SortType.TopRated,
    val isLoading: Boolean = true
)

sealed class HomeEvent {
    object Init : HomeEvent()
    object GetNextPage : HomeEvent()
    object ToggleSorting : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTVShows: GetTVShows
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var lastPage: Page? = null
    private val pagedItems: MutableMap<Int, List<TVShow>> = mutableMapOf()

    sealed class UiEvent {
        data class ShowSnackbar(@StringRes val message: Int) : UiEvent()
    }

    private var getTVShowsJob: Job? = null

    init {
        dispatch(HomeEvent.Init)
    }

    fun dispatch(event: HomeEvent) {
        when (event) {
            HomeEvent.Init -> getShows(refresh = true)
            HomeEvent.GetNextPage -> getShows(refresh = false)
            HomeEvent.ToggleSorting -> toggleSorting()
        }
    }

    private fun getShows(refresh: Boolean) {
        if (refresh) {
            lastPage = null
        } else if (lastPage?.items?.isEmpty() == true) {
            return
        }

        getTVShowsJob?.cancel()
        _state.value = state.value.copy(isLoading = true)
        getTVShowsJob = getTVShows(lastPage)
            .onEach { page ->
                lastPage = page
                pagedItems[page.pageIndex] = page.items
                updateItems(_state.value.sortType)
            }
            .catch {
                _state.value = state.value.copy(isLoading = false)
                _eventFlow.emit(UiEvent.ShowSnackbar(message = getErrorMessage(it)))
            }
            .launchIn(viewModelScope)
    }

    private fun toggleSorting() {
        val sortType = when (_state.value.sortType) {
            SortType.TopRated -> SortType.Alphabetic
            SortType.Alphabetic -> SortType.TopRated
        }
        updateItems(sortType)
    }

    private fun updateItems(sortType: SortType) {
        when (sortType) {
            SortType.TopRated -> {
                _state.value = state.value.copy(
                    isLoading = false,
                    sortType = sortType,
                    list = pagedItems.toSortedMap().values.flatten()
                )
            }

            SortType.Alphabetic -> {
                _state.value = state.value.copy(
                    isLoading = false,
                    sortType = sortType,
                    list = pagedItems.toSortedMap().values.flatten().sortedBy { it.title }
                )
            }
        }
    }

    private fun getErrorMessage(error: Throwable): Int {
        return when (error) {
            is Failure.NetworkFailure -> R.string.failure_network
            else -> R.string.failure_generic
        }
    }
}
