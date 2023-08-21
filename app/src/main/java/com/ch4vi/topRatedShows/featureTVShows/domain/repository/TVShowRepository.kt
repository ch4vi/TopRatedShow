package com.ch4vi.topRatedShows.featureTVShows.domain.repository

import com.ch4vi.topRatedShows.featureTVShows.domain.model.Page

interface TVShowRepository {

    suspend fun getTopRatedTVShows(page: Int): Page
}
