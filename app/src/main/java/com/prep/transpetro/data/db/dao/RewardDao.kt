package com.prep.transpetro.data.db.dao

import androidx.room.*
import com.prep.transpetro.data.db.entity.Reward
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards ORDER BY id ASC")
    fun getAllFlow(): Flow<List<Reward>>

    @Query("SELECT * FROM rewards WHERE unlockedAt IS NULL ORDER BY id ASC LIMIT 1")
    suspend fun getNextLocked(): Reward?

    @Upsert
    suspend fun upsert(reward: Reward)

    @Update
    suspend fun update(reward: Reward)

    @Query("UPDATE rewards SET unlockedAt = :date WHERE id = :id")
    suspend fun unlock(id: Long, date: String)

    @Query("SELECT COUNT(*) FROM rewards WHERE unlockedAt IS NULL")
    fun countLockedFlow(): Flow<Int>
}
