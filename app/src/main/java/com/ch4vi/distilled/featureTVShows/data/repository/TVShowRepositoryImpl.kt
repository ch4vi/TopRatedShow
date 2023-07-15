package com.ch4vi.distilled.featureTVShows.data.repository

import com.ch4vi.distilled.featureTVShows.data.api.datasource.TVShowApiDataSource
import com.ch4vi.distilled.featureTVShows.domain.model.Page
import com.ch4vi.distilled.featureTVShows.domain.repository.TVShowRepository
import javax.inject.Inject

class TVShowRepositoryImpl @Inject constructor(
    private val apiDataSource: TVShowApiDataSource
) : TVShowRepository {

    override suspend fun getTopRatedTVShows(page: Int): Page {
        return apiDataSource.fetchTopRatedTVShows(page)
    }
}
