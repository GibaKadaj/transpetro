package com.prep.transpetro.data.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.prep.transpetro.data.db.entity.*
import com.prep.transpetro.domain.model.StudyConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeedCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getInstance(context)
            seedBlocks(database)
            seedDailyPlan(database)
            seedErrorLogExamples(database)
            seedPlannedSimulados(database)
            seedRewards(database)
        }
    }

    private suspend fun seedBlocks(db: AppDatabase) {
        val dao = db.blockCoverageDao()
        StudyConstants.BLOCKS.forEach { block ->
            dao.upsert(
                BlockCoverage(
                    blockCode = block.code,
                    name = block.name,
                    weight = block.weight,
                    plannedWeeks = block.plannedWeeks,
                    status = "NAO_INICIADO"
                )
            )
        }
    }

    private suspend fun seedDailyPlan(db: AppDatabase) {
        val dao = db.dailyPlanDao()
        val plans = buildDailyPlanList()
        plans.forEach { dao.upsert(it) }
    }

    private fun buildDailyPlanList(): List<DailyPlan> {
        val result = mutableListOf<DailyPlan>()
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE
        var current = LocalDate.parse(StudyConstants.PREP_START_DATE, fmt)
        val end = LocalDate.parse(StudyConstants.EXAM_DATE, fmt)

        val explicitDays = mapOf(
            "2026-09-14" to DailyPlan("2026-09-14", "DIAG", "Simulado diagnóstico (40 questões de Segurança, cronometrado)"),
            "2026-09-15" to DailyPlan("2026-09-15", "DIAG", "Diagnóstico: 10 Português + 10 Matemática"),
            "2026-09-16" to DailyPlan("2026-09-16", "REV",  "Correção e análise dos diagnósticos"),
            "2026-09-17" to DailyPlan("2026-09-17", "B4",   "B4 — Legislação e Normas Técnicas"),
            "2026-09-18" to DailyPlan("2026-09-18", "REV",  "Revisão (Log de Erros)"),
            "2026-09-19" to DailyPlan("2026-09-19", "B4",   "B4 — Legislação e Normas Técnicas"),
            "2026-09-20" to DailyPlan("2026-09-20", "PT",   "Português + Matemática"),
            "2026-09-21" to DailyPlan("2026-09-21", "B1",   "B1 — Prevenção e Controle de Riscos"),
            "2026-09-22" to DailyPlan("2026-09-22", "PT",   "Português"),
            "2026-09-23" to DailyPlan("2026-09-23", "B1",   "B1 — Prevenção e Controle de Riscos"),
            "2026-09-24" to DailyPlan("2026-09-24", "MT",   "Matemática"),
            "2026-09-25" to DailyPlan("2026-09-25", "REV",  "Revisão"),
            "2026-09-26" to DailyPlan("2026-09-26", "B1",   "B1 — Prevenção e Controle de Riscos"),
            "2026-09-27" to DailyPlan("2026-09-27", "B1",   "B1 + Português")
        )

        while (!current.isAfter(end)) {
            val key = current.format(fmt)
            val plan = explicitDays[key] ?: generateDefaultPlan(current)
            result.add(plan)
            current = current.plusDays(1)
        }
        return result
    }

    private fun generateDefaultPlan(date: LocalDate): DailyPlan {
        val key = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val blockCode = when (date.dayOfWeek.value) {
            2 -> "PT"
            4 -> "MT"
            5 -> "REV"
            6 -> "SIM"
            7 -> "SIM"
            else -> "B4"
        }
        val desc = when (blockCode) {
            "PT"  -> "Português"
            "MT"  -> "Matemática"
            "REV" -> "Revisão"
            "SIM" -> "Simulado / Revisão"
            else  -> "Segurança"
        }
        return DailyPlan(key, blockCode, desc)
    }

    private suspend fun seedErrorLogExamples(db: AppDatabase) {
        val dao = db.errorLogDao()
        listOf(
            ErrorLogEntry(
                date = "2026-09-14",
                blockCode = "B1",
                topicNorm = "NR-33 Espaço Confinado",
                myAnswer = "C",
                correctAnswer = "D",
                errorReason = "DESCONHECIMENTO",
                triggerWord = "pode entrar",
                correctionLine = "O vigia JAMAIS entra no espaço confinado. Sem exceção."
            ),
            ErrorLogEntry(
                date = "2026-09-14",
                blockCode = "BT",
                topicNorm = "Taxa de Gravidade",
                myAnswer = "B — 1.250",
                correctAnswer = "C",
                errorReason = "CALCULO",
                triggerWord = "dias debitados",
                correctionLine = "TG = (perdidos + debitados) × 1.000.000 / HHT."
            ),
            ErrorLogEntry(
                date = "2026-09-14",
                blockCode = "B2",
                topicNorm = "NR-15 Anexo 1 Ruído",
                myAnswer = "A — 90 dB(A)",
                correctAnswer = "B",
                errorReason = "PEGADINHA",
                triggerWord = "85 dB(A)",
                correctionLine = "Limite para 8h é 85 dB(A). A banca troca por 90 para confundir."
            )
        ).forEach { dao.insert(it) }
    }

    private suspend fun seedPlannedSimulados(db: AppDatabase) {
        val dao = db.simuladoDao()
        StudyConstants.PLANNED_SIMULADOS.forEach { (date, week, type) ->
            dao.insert(
                Simulado(
                    date = date,
                    weekNumber = week,
                    type = type,
                    securityCorrect = 0,
                    portugueseCorrect = 0,
                    mathCorrect = 0,
                    isPlanned = true
                )
            )
        }
    }

    private suspend fun seedRewards(db: AppDatabase) {
        val dao = db.rewardDao()
        StudyConstants.DEFAULT_REWARDS.forEach { (key, desc) ->
            dao.upsert(
                Reward(
                    milestoneKey = key,
                    description = desc,
                    rewardText = "Defina sua recompensa",
                    unlockedAt = null
                )
            )
        }
    }
}
