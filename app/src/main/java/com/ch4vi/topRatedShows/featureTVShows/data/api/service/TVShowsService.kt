package com.ch4vi.topRatedShows.featureTVShows.data.api.service

import com.ch4vi.topRatedShows.common.constants.Constants
import com.ch4vi.topRatedShows.common.constants.Constants.START_PAGE
import com.ch4vi.topRatedShows.featureTVShows.data.api.model.PageDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface TVShowsService {

    companion object {
        private const val TOP_RATED_TV_SHOW = "tv/top_rated"
        private const val LANGUAGE = "en-US"
    }

    @GET(TOP_RATED_TV_SHOW)
    suspend fun getTopRatedTVShows(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: Int = START_PAGE
    ): PageDTO
}
