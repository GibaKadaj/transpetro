package com.prep.transpetro.data.repository

import com.prep.transpetro.data.db.dao.BlockCoverageDao
import com.prep.transpetro.data.db.entity.BlockCoverage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockCoverageRepository @Inject constructor(
    private val dao: BlockCoverageDao
) {
    fun getAllFlow(): Flow<List<BlockCoverage>> = dao.getAllFlow()
    suspend fun getByCode(code: String) = dao.getByCode(code)
    suspend fun getNotStarted() = dao.getNotStarted()
    suspend fun upsert(block: BlockCoverage) = dao.upsert(block)
    suspend fun update(block: BlockCoverage) = dao.update(block)
}
