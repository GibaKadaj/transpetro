package com.prep.transpetro.data.repository

import com.prep.transpetro.data.db.dao.RewardDao
import com.prep.transpetro.data.db.entity.Reward
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardRepository @Inject constructor(
    private val dao: RewardDao
) {
    fun getAllFlow(): Flow<List<Reward>> = dao.getAllFlow()
    fun countLockedFlow() = dao.countLockedFlow()
    suspend fun getNextLocked() = dao.getNextLocked()
    suspend fun upsert(reward: Reward) = dao.upsert(reward)
    suspend fun update(reward: Reward) = dao.update(reward)
    suspend fun unlock(id: Long, date: String) = dao.unlock(id, date)
}
