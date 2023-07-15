package com.ch4vi.distilled.featureTVShows.domain.model

data class Page(
    val pageIndex: Int,
    val totalPages: Int,
    val items: List<TVShow>
)
