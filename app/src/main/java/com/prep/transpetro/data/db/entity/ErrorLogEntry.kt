package com.prep.transpetro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "error_logs")
data class ErrorLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val blockCode: String,
    val topicNorm: String,
    val myAnswer: String,
    val correctAnswer: String,
    val errorReason: String,
    val triggerWord: String,
    val correctionLine: String
)
