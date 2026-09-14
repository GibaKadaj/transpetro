package com.prep.transpetro.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val milestoneKey: String,
    val description: String,
    val rewardText: String,
    val unlockedAt: String?
)
