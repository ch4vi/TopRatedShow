package com.ch4vi.topRatedShows.featureTVShows.data.api.model

import com.squareup.moshi.Json

data class TVShowDTO(
    val id: Int? = null,
    val name: String? = null,
    @Json(name = "poster_path") val image: String? = null
)

