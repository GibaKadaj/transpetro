package com.prep.transpetro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_records")
data class DailyRecord(
    @PrimaryKey val date: String,
    val blockCode: String,
    val status: String,
    val hoursStudied: Double,
    val questionsDone: Int,
    val correctAnswers: Int,
    val energyLevel: Int,
    val notes: String?
)
