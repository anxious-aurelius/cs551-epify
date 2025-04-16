package com.jetpack.myapplication.data


import kotlinx.coroutines.flow.Flow

class WatchlistRepository(private val dao: WatchlistDao) {
    fun getWatchlist(): Flow<List<WatchlistEntity>> = dao.getAll()
    suspend fun addItem(item: WatchlistEntity) = dao.insert(item)
    suspend fun removeItem(item: WatchlistEntity) = dao.delete(item)
}
