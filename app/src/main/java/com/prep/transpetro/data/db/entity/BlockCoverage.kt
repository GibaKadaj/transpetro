package com.prep.transpetro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "block_coverage")
data class BlockCoverage(
    @PrimaryKey val blockCode: String,
    val name: String,
    val weight: Int,
    val plannedWeeks: String,
    val status: String,
    val questionsDone: Int = 0,
    val questionsCorrect: Int = 0
)
