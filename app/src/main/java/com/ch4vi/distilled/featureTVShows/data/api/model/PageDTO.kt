package com.ch4vi.distilled.featureTVShows.data.api.model

import com.squareup.moshi.Json

data class PageDTO(
    @Json(name = "page") val pageIndex: Int? = null,
    @Json(name = "total_pages") val totalPages: Int? = null,
    @Json(name = "results") val items: List<TVShowDTO>? = null
)
