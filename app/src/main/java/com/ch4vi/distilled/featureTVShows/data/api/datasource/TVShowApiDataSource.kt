package com.ch4vi.distilled.featureTVShows.data.api.datasource

import com.ch4vi.distilled.featureTVShows.data.api.datasource.TVShowApiMapper.toDomain
import com.ch4vi.distilled.featureTVShows.data.api.service.TVShowsService
import com.ch4vi.distilled.featureTVShows.domain.model.Failure
import com.ch4vi.distilled.featureTVShows.domain.model.Page
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class TVShowApiDataSource @Inject constructor(
    private val service: TVShowsService
) {

    suspend fun fetchTopRatedTVShows(page: Int): Page {
        return try {
            service.getTopRatedTVShows(page = page).toDomain()
        } catch (t: Throwable) {
            throw onError(t)
        }
    }

    private fun onError(failure: Throwable): Throwable {
        return when (failure) {
            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException,
            is HttpException -> Failure.NetworkFailure(failure.message)

            is Failure -> failure
            else -> Failure.UnexpectedFailure(failure.message)
        }
    }
}
