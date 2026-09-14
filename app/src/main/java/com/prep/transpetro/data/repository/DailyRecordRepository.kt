package com.prep.transpetro.data.repository

import com.prep.transpetro.data.db.dao.DailyRecordDao
import com.prep.transpetro.data.db.entity.DailyRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyRecordRepository @Inject constructor(
    private val dao: DailyRecordDao
) {
    fun getAllFlow(): Flow<List<DailyRecord>> = dao.getAllFlow()
    fun getInRangeFlow(start: String, end: String) = dao.getInRangeFlow(start, end)
    suspend fun getByDate(date: String) = dao.getByDate(date)
    suspend fun getStudiedDates() = dao.getStudiedDates()
    suspend fun upsert(record: DailyRecord) = dao.upsert(record)
    suspend fun delete(record: DailyRecord) = dao.delete(record)
    suspend fun getMostRecent() = dao.getMostRecent()
}
