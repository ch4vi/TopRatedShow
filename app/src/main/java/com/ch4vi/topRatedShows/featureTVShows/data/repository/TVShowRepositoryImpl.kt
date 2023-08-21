package com.ch4vi.topRatedShows.featureTVShows.data.repository

import com.ch4vi.topRatedShows.featureTVShows.data.api.datasource.TVShowApiDataSource
import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page
import com.ch4vi.topRatedShows.featureTVShows.domain.repository.TVShowRepository
import javax.inject.Inject

class TVShowRepositoryImpl @Inject constructor(
    private val apiDataSource: TVShowApiDataSource
) : TVShowRepository {

    override suspend fun getTopRatedTVShows(page: Int): Page {
        return apiDataSource.fetchTopRatedTVShows(page)
    }
}
