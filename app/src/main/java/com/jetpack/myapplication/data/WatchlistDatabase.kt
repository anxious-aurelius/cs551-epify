package com.jetpack.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WatchlistEntity::class, WatchedEpisodeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
    abstract fun episodeDao(): EpisodeDao

    companion object {
        @Volatile
        private var INSTANCE: WatchlistDatabase? = null

        fun getDatabase(context: Context): WatchlistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                WatchlistDatabase::class.java,
                                "watchlist_db"
                            ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
