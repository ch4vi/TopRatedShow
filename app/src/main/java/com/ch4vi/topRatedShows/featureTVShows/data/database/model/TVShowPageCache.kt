package com.ch4vi.topRatedShows.featureTVShows.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_shows_cache")
data class TVShowPageCache(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "page_index") val page: Int,
    @ColumnInfo(name = "page_total") val totalPages: Int,
)
