package com.prep.transpetro.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.repository.BlockCoverageRepository
import com.prep.transpetro.data.repository.DailyPlanRepository
import com.prep.transpetro.data.repository.DailyRecordRepository
import com.prep.transpetro.data.repository.SimuladoRepository
import com.prep.transpetro.domain.model.StudyConstants
import com.prep.transpetro.domain.usecase.ComputeStreakUseCase
import com.prep.transpetro.domain.usecase.EvaluateCutoffUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class HomeUiState(
    val todayDate: String = "",
    val daysUntilExam: Long = 0,
    val currentPhase: String = "",
    val currentStreak: Int = 0,
    val hasConsecutiveMissAlert: Boolean = false,
    val consistencyPct: Float = 0f,
    val todayPlanDescription: String = "",
    val todayPlanBlock: String = "",
    val lastSimuladoSecurity: Int? = null,
    val lastSimuladoPortuguese: Int? = null,
    val lastSimuladoMath: Int? = null,
    val lastSimuladoCutoffLabel: String = "",
    val blocksStudied: Int = 0,
    val totalBlocks: Int = 17,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recordRepository: DailyRecordRepository,
    private val planRepository: DailyPlanRepository,
    private val simuladoRepository: SimuladoRepository,
    private val blockRepository: BlockCoverageRepository,
    private val computeStreak: ComputeStreakUseCase,
    private val evaluateCutoff: EvaluateCutoffUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        viewModelScope.launch {
            val today = LocalDate.now()
            val examDate = LocalDate.parse(StudyConstants.EXAM_DATE)
            val daysLeft = ChronoUnit.DAYS.between(today, examDate).coerceAtLeast(0)
            val phase = StudyConstants.phaseFor(today.toString())

            combine(
                recordRepository.getAllFlow(),
                planRepository.getAllFlow(),
                simuladoRepository.getAllFlow(),
                blockRepository.getAllFlow()
            ) { records, plans, simulados, blocks ->
                val studiedDates = records.filter { it.status != "NAO" }.map { it.date }.toSet()
                val streak = computeStreak(studiedDates)
                val todayPlan = plans.firstOrNull { it.date == today.toString() }
                val lastSim = simulados.filter { !it.isPlanned }
                    .maxByOrNull { it.date }
                val cutoffLabel = if (lastSim != null) {
                    when (evaluateCutoff(lastSim.securityCorrect, lastSim.portugueseCorrect, lastSim.mathCorrect)) {
                        is com.prep.transpetro.domain.model.CutoffStatus.PassesAll -> "Aprovado"
                        is com.prep.transpetro.domain.model.CutoffStatus.EliminatedZero -> "Eliminado (zero)"
                        is com.prep.transpetro.domain.model.CutoffStatus.BelowSecurityMinimum -> "Abaixo min. seguranca"
                        is com.prep.transpetro.domain.model.CutoffStatus.BelowGeneralMinimum -> "Abaixo min. geral"
                    }
                } else ""
                val studiedBlocks = blocks.count { it.status != "NAO_INICIADO" }

                HomeUiState(
                    todayDate = today.toString(),
                    daysUntilExam = daysLeft,
                    currentPhase = phase,
                    currentStreak = streak.currentStreak,
                    hasConsecutiveMissAlert = streak.hasConsecutiveMissAlert,
                    consistencyPct = streak.consistencyPct,
                    todayPlanDescription = todayPlan?.description ?: "",
                    todayPlanBlock = todayPlan?.blockCode ?: "",
                    lastSimuladoSecurity = lastSim?.securityCorrect,
                    lastSimuladoPortuguese = lastSim?.portugueseCorrect,
                    lastSimuladoMath = lastSim?.mathCorrect,
                    lastSimuladoCutoffLabel = cutoffLabel,
                    blocksStudied = studiedBlocks,
                    totalBlocks = blocks.size,
                    isLoading = false
                )
            }.collect { newState ->
                _state.update { newState.copy(daysUntilExam = daysLeft, currentPhase = phase) }
            }
        }
    }
}
