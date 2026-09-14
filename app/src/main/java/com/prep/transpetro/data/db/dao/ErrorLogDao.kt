package com.prep.transpetro.data.db.dao

import androidx.room.*
import com.prep.transpetro.data.db.entity.ErrorLogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ErrorLogDao {
    @Query("SELECT * FROM error_logs ORDER BY date DESC, id DESC")
    fun getAllFlow(): Flow<List<ErrorLogEntry>>

    @Query("SELECT * FROM error_logs WHERE blockCode = :blockCode ORDER BY date DESC")
    fun getByBlockFlow(blockCode: String): Flow<List<ErrorLogEntry>>

    @Query("SELECT * FROM error_logs WHERE errorReason = :reason ORDER BY date DESC")
    fun getByReasonFlow(reason: String): Flow<List<ErrorLogEntry>>

    @Query("SELECT * FROM error_logs WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getInRangeFlow(start: String, end: String): Flow<List<ErrorLogEntry>>

    @Query("SELECT * FROM error_logs WHERE topicNorm LIKE '%' || :query || '%' OR triggerWord LIKE '%' || :query || '%' OR correctionLine LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchFlow(query: String): Flow<List<ErrorLogEntry>>

    @Query("SELECT COUNT(*) FROM error_logs")
    fun countFlow(): Flow<Int>

    @Query("SELECT errorReason, COUNT(*) as cnt FROM error_logs GROUP BY errorReason ORDER BY cnt DESC")
    suspend fun countByReason(): List<ReasonCount>

    @Query("SELECT blockCode, COUNT(*) as cnt FROM error_logs GROUP BY blockCode ORDER BY cnt DESC")
    suspend fun countByBlock(): List<BlockCount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: ErrorLogEntry): Long

    @Update
    suspend fun update(entry: ErrorLogEntry)

    @Delete
    suspend fun delete(entry: ErrorLogEntry)

    @Query("SELECT * FROM error_logs WHERE id = :id")
    suspend fun getById(id: Long): ErrorLogEntry?
}

data class ReasonCount(val errorReason: String, val cnt: Int)
data class BlockCount(val blockCode: String, val cnt: Int)
