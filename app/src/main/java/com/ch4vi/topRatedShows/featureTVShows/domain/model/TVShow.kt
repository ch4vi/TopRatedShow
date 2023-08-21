package com.ch4vi.topRatedShows.featureTVShows.domain.model

import com.ch4vi.topRatedShows.common.constants.Constants

data class TVShow(
    val id: Int,
    val title: String,
    val image: String
) {
    val imageUrl: String
        get() = "${Constants.IMAGE_BASE_URL}$image"
}
