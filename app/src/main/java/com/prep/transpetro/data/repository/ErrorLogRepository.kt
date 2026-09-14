package com.prep.transpetro.data.repository

import com.prep.transpetro.data.db.dao.ErrorLogDao
import com.prep.transpetro.data.db.entity.ErrorLogEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorLogRepository @Inject constructor(
    private val dao: ErrorLogDao
) {
    fun getAllFlow(): Flow<List<ErrorLogEntry>> = dao.getAllFlow()
    fun getByBlockFlow(blockCode: String) = dao.getByBlockFlow(blockCode)
    fun getByReasonFlow(reason: String) = dao.getByReasonFlow(reason)
    fun getInRangeFlow(start: String, end: String) = dao.getInRangeFlow(start, end)
    fun searchFlow(query: String) = dao.searchFlow(query)
    fun countFlow() = dao.countFlow()
    suspend fun countByReason() = dao.countByReason()
    suspend fun countByBlock() = dao.countByBlock()
    suspend fun insert(entry: ErrorLogEntry) = dao.insert(entry)
    suspend fun update(entry: ErrorLogEntry) = dao.update(entry)
    suspend fun delete(entry: ErrorLogEntry) = dao.delete(entry)
    suspend fun getById(id: Long) = dao.getById(id)
}
