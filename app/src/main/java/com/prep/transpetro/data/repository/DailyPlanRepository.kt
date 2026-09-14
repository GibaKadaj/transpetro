package com.prep.transpetro.data.repository

import com.prep.transpetro.data.db.dao.DailyPlanDao
import com.prep.transpetro.data.db.entity.DailyPlan
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyPlanRepository @Inject constructor(
    private val dao: DailyPlanDao
) {
    fun getAllFlow(): Flow<List<DailyPlan>> = dao.getAllFlow()
    suspend fun getByDate(date: String) = dao.getByDate(date)
    fun getInRangeFlow(start: String, end: String) = dao.getInRangeFlow(start, end)
    suspend fun upsert(plan: DailyPlan) = dao.upsert(plan)
}
