package com.prep.transpetro.data.db.dao

import androidx.room.*
import com.prep.transpetro.data.db.entity.BlockCoverage
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockCoverageDao {
    @Query("SELECT * FROM block_coverage ORDER BY blockCode ASC")
    fun getAllFlow(): Flow<List<BlockCoverage>>

    @Query("SELECT * FROM block_coverage WHERE blockCode = :code")
    suspend fun getByCode(code: String): BlockCoverage?

    @Query("SELECT * FROM block_coverage WHERE status = 'NAO_INICIADO'")
    suspend fun getNotStarted(): List<BlockCoverage>

    @Upsert
    suspend fun upsert(block: BlockCoverage)

    @Update
    suspend fun update(block: BlockCoverage)
}
