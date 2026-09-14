package com.prep.transpetro.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.prep.transpetro.data.db.dao.*
import com.prep.transpetro.data.db.entity.*

@Database(
    entities = [
        DailyRecord::class,
        ErrorLogEntry::class,
        Simulado::class,
        BlockCoverage::class,
        DailyPlan::class,
        Reward::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyRecordDao(): DailyRecordDao
    abstract fun errorLogDao(): ErrorLogDao
    abstract fun simuladoDao(): SimuladoDao
    abstract fun blockCoverageDao(): BlockCoverageDao
    abstract fun dailyPlanDao(): DailyPlanDao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "transpetro_prep.db"
                )
                    .addCallback(SeedCallback(context))
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
