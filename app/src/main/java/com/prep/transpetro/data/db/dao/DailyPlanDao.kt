package com.prep.transpetro.data.db.dao

import androidx.room.*
import com.prep.transpetro.data.db.entity.DailyPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyPlanDao {
    @Query("SELECT * FROM daily_plan WHERE date = :date")
    suspend fun getByDate(date: String): DailyPlan?

    @Query("SELECT * FROM daily_plan ORDER BY date ASC")
    fun getAllFlow(): Flow<List<DailyPlan>>

    @Upsert
    suspend fun upsert(plan: DailyPlan)

    @Query("SELECT * FROM daily_plan WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    fun getInRangeFlow(start: String, end: String): Flow<List<DailyPlan>>
}
