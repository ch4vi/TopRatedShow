package com.ch4vi.topRatedShows.featureTVShows.data.database.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ch4vi.topRatedShows.featureTVShows.data.database.model.TVShowPageCache
import kotlinx.coroutines.flow.Flow

@Dao
interface TVShowPageDao {

    @Query("SELECT * FROM tv_shows_cache WHERE page_index = :page")
    fun getTVShowsByPage(page: Int): Flow<List<TVShowPageCache>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplates(templates: List<TVShowPageCache>)

    @Query("UPDATE tv_shows_cache SET name = :name, image = :image, page_index = :page, page_total = :totalPages WHERE id = :id")
    suspend fun update(id: String, name: String, image: String?, page: Int, totalPages: Int)

    @Query("DELETE FROM tv_shows_cache")
    suspend fun clearAll()
}
