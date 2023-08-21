package com.ch4vi.topRatedShows.featureTVShows.domain.model

data class Page(
    val pageIndex: Int,
    val totalPages: Int,
    val items: List<TVShow>
)
