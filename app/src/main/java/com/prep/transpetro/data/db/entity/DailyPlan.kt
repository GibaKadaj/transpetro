package com.prep.transpetro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_plan")
data class DailyPlan(
    @PrimaryKey val date: String,
    val blockCode: String,
    val description: String,
    val isCustom: Boolean = false
)
