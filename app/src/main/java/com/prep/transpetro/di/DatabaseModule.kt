package com.prep.transpetro.di

import android.content.Context
import androidx.room.Room
import com.prep.transpetro.data.db.AppDatabase
import com.prep.transpetro.data.db.SeedCallback
import com.prep.transpetro.data.db.dao.BlockCoverageDao
import com.prep.transpetro.data.db.dao.DailyPlanDao
import com.prep.transpetro.data.db.dao.DailyRecordDao
import com.prep.transpetro.data.db.dao.ErrorLogDao
import com.prep.transpetro.data.db.dao.RewardDao
import com.prep.transpetro.data.db.dao.SimuladoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        seedCallback: SeedCallback
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "transpetro.db")
        .addCallback(seedCallback)
        .build()

    @Provides fun provideDailyRecordDao(db: AppDatabase): DailyRecordDao = db.dailyRecordDao()
    @Provides fun provideErrorLogDao(db: AppDatabase): ErrorLogDao = db.errorLogDao()
    @Provides fun provideSimuladoDao(db: AppDatabase): SimuladoDao = db.simuladoDao()
    @Provides fun provideDailyPlanDao(db: AppDatabase): DailyPlanDao = db.dailyPlanDao()
    @Provides fun provideRewardDao(db: AppDatabase): RewardDao = db.rewardDao()
    @Provides fun provideBlockCoverageDao(db: AppDatabase): BlockCoverageDao = db.blockCoverageDao()
}
