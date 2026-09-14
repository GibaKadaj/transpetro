package com.prep.transpetro.data.db.dao

import androidx.room.*
import com.prep.transpetro.data.db.entity.DailyRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyRecordDao {
    @Query("SELECT * FROM daily_records WHERE date = :date")
    suspend fun getByDate(date: String): DailyRecord?

    @Query("SELECT * FROM daily_records ORDER BY date DESC")
    fun getAllFlow(): Flow<List<DailyRecord>>

    @Query("SELECT * FROM daily_records WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    fun getInRangeFlow(start: String, end: String): Flow<List<DailyRecord>>

    @Query("SELECT date FROM daily_records WHERE status != 'NAO' ORDER BY date ASC")
    suspend fun getStudiedDates(): List<String>

    @Upsert
    suspend fun upsert(record: DailyRecord)

    @Delete
    suspend fun delete(record: DailyRecord)

    @Query("SELECT * FROM daily_records ORDER BY date DESC LIMIT 1")
    suspend fun getMostRecent(): DailyRecord?
}
