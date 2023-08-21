package com.ch4vi.topRatedShows.featureTVShows.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ch4vi.topRatedShows.featureTVShows.data.database.datasource.TVShowPageDao
import com.ch4vi.topRatedShows.featureTVShows.data.database.model.TVShowPageCache

@Database(
    entities = [TVShowPageCache::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val NAME = "appDB"
    }

    abstract fun tvShowPageDao(): TVShowPageDao
}
