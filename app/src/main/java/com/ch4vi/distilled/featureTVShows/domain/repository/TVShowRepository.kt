package com.ch4vi.distilled.featureTVShows.domain.repository

import com.ch4vi.distilled.featureTVShows.domain.model.Page

interface TVShowRepository {

    suspend fun getTopRatedTVShows(page: Int): Page
}
