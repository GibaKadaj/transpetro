package com.prep.transpetro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "simulados")
data class Simulado(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val weekNumber: Int,
    val type: String,
    val securityCorrect: Int,
    val portugueseCorrect: Int,
    val mathCorrect: Int,
    val isPlanned: Boolean = false
)
