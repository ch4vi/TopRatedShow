package com.ch4vi.topRatedShows.featureTVShows.domain.usecase

import com.ch4vi.topRatedShows.common.constants.Constants.START_PAGE
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.repository.TVShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTVShows @Inject constructor(
    private val repository: TVShowRepository
) {
    operator fun invoke(page: Page?): Flow<Page> = flow {
        when {
            page == null -> emit(repository.getTopRatedTVShows(START_PAGE))
            page.pageIndex == page.totalPages -> emit(page.copy(items = emptyList()))
            else -> emit(repository.getTopRatedTVShows(page.pageIndex + 1))
        }
    }
}
