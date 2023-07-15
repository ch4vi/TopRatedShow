package com.ch4vi.distilled.featureTVShows.data.api.model

import com.squareup.moshi.Json

data class TVShowDTO(
    val id: Int? = null,
    val name: String? = null,
    @Json(name = "poster_path") val image: String? = null
)

data class PageDTO(
    @Json(name = "page") val pageIndex: Int? = null,
    @Json(name = "total_pages") val totalPages: Int? = null,
    @Json(name = "results") val items: List<TVShowDTO>? = null
)
