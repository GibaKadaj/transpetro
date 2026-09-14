package com.prep.transpetro.domain.model

data class StreakStatus(
    val studiedDates: Set<String>,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalStudied: Int,
    val totalLost: Int,
    val consistencyPct: Float,
    val hasConsecutiveMissAlert: Boolean
)
