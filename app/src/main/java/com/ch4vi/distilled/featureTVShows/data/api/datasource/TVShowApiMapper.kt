package com.ch4vi.distilled.featureTVShows.data.api.datasource

import com.ch4vi.distilled.featureTVShows.data.api.model.PageDTO
import com.ch4vi.distilled.featureTVShows.data.api.model.TVShowDTO
import com.ch4vi.distilled.featureTVShows.domain.model.Failure
import com.ch4vi.distilled.featureTVShows.domain.model.Page
import com.ch4vi.distilled.featureTVShows.domain.model.TVShow

object TVShowApiMapper {

    @Throws(Failure.MapperFailure::class)
    fun TVShowDTO.toDomain(): TVShow {
        if (id == null || name == null) throw Failure.MapperFailure
        return TVShow(id, name, image ?: "")
    }

    @Throws(Failure.MapperFailure::class)
    fun PageDTO.toDomain(): Page {
        if (pageIndex == null || totalPages == null) throw Failure.MapperFailure
        return Page(
            pageIndex = pageIndex,
            totalPages = totalPages,
            items = items?.map { it.toDomain() } ?: emptyList()
        )
    }
}
