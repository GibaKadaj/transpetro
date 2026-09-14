package com.prep.transpetro.ui.screens.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.DailyRecord
import com.prep.transpetro.data.repository.DailyPlanRepository
import com.prep.transpetro.data.repository.DailyRecordRepository
import com.prep.transpetro.domain.usecase.ComputeAccuracyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DailyRecordUiState(
    val date: String = LocalDate.now().toString(),
    val blockCode: String = "",
    val status: String = "NAO",
    val hoursStudied: Double = 0.0,
    val questionsDone: Int = 0,
    val correctAnswers: Int = 0,
    val energyLevel: Int = 3,
    val notes: String = "",
    val accuracy: Float = 0f,
    val todayPlanDescription: String = "",
    val isSaved: Boolean = false
)

@HiltViewModel
class DailyRecordViewModel @Inject constructor(
    private val recordRepository: DailyRecordRepository,
    private val planRepository: DailyPlanRepository,
    private val computeAccuracy: ComputeAccuracyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyRecordUiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DailyRecordUiState())

    init {
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            val existing = recordRepository.getByDate(today)
            val plan = planRepository.getByDate(today)
            _state.update { s ->
                s.copy(
                    blockCode = existing?.blockCode ?: plan?.blockCode ?: "",
                    status = existing?.status ?: "NAO",
                    hoursStudied = existing?.hoursStudied ?: 0.0,
                    questionsDone = existing?.questionsDone ?: 0,
                    correctAnswers = existing?.correctAnswers ?: 0,
                    energyLevel = existing?.energyLevel ?: 3,
                    notes = existing?.notes ?: "",
                    accuracy = computeAccuracy(existing?.questionsDone ?: 0, existing?.correctAnswers ?: 0),
                    todayPlanDescription = plan?.description ?: ""
                )
            }
        }
    }

    fun setStatus(status: String) = _state.update { it.copy(status = status) }
    fun setHours(h: Double) = _state.update { it.copy(hoursStudied = h) }
    fun setQuestions(q: Int) = _state.update {
        val acc = computeAccuracy(q, it.correctAnswers)
        it.copy(questionsDone = q, accuracy = acc)
    }
    fun setCorrect(c: Int) = _state.update {
        val acc = computeAccuracy(it.questionsDone, c)
        it.copy(correctAnswers = c, accuracy = acc)
    }
    fun setEnergy(e: Int) = _state.update { it.copy(energyLevel = e) }
    fun setNotes(n: String) = _state.update { it.copy(notes = n) }
    fun setBlockCode(b: String) = _state.update { it.copy(blockCode = b) }

    fun save() {
        val s = _state.value
        viewModelScope.launch {
            recordRepository.upsert(
                DailyRecord(
                    date = s.date,
                    blockCode = s.blockCode,
                    status = s.status,
                    hoursStudied = s.hoursStudied,
                    questionsDone = s.questionsDone,
                    correctAnswers = s.correctAnswers,
                    energyLevel = s.energyLevel,
                    notes = s.notes.ifBlank { null }
                )
            )
            _state.update { it.copy(isSaved = true) }
        }
    }
}
