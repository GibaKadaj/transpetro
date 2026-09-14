package com.prep.transpetro.ui.screens.simulado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.Simulado
import com.prep.transpetro.data.repository.SimuladoRepository
import com.prep.transpetro.domain.model.CutoffStatus
import com.prep.transpetro.domain.model.StudyConstants
import com.prep.transpetro.domain.usecase.EvaluateCutoffUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class SimuladoDetailUiState(
    val id: Long = 0L,
    val date: String = LocalDate.now().toString(),
    val weekNumber: Int = 1,
    val type: String = "PARCIAL",
    val securityCorrect: Int = 0,
    val portugueseCorrect: Int = 0,
    val mathCorrect: Int = 0,
    val isPlanned: Boolean = false,
    val cutoffStatus: CutoffStatus = CutoffStatus.PassesAll,
    val isSaved: Boolean = false,
    val isNew: Boolean = true
)

@HiltViewModel
class SimuladoDetailViewModel @Inject constructor(
    private val repository: SimuladoRepository,
    private val evaluateCutoff: EvaluateCutoffUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SimuladoDetailUiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SimuladoDetailUiState())

    fun load(id: Long) {
        if (id <= 0L) return
        viewModelScope.launch {
            val sim = repository.getById(id) ?: return@launch
            _state.update {
                it.copy(
                    id = sim.id,
                    date = sim.date,
                    weekNumber = sim.weekNumber,
                    type = sim.type,
                    securityCorrect = sim.securityCorrect,
                    portugueseCorrect = sim.portugueseCorrect,
                    mathCorrect = sim.mathCorrect,
                    isPlanned = sim.isPlanned,
                    cutoffStatus = evaluateCutoff(sim.securityCorrect, sim.portugueseCorrect, sim.mathCorrect),
                    isNew = false
                )
            }
        }
    }

    fun setSecurity(v: Int) = update(securityCorrect = v.coerceIn(0, StudyConstants.SECURITY_QUESTIONS))
    fun setPortuguese(v: Int) = update(portugueseCorrect = v.coerceIn(0, StudyConstants.PORTUGUESE_QUESTIONS))
    fun setMath(v: Int) = update(mathCorrect = v.coerceIn(0, StudyConstants.MATH_QUESTIONS))
    fun setDate(d: String) = _state.update { it.copy(date = d) }
    fun setWeek(w: Int) = _state.update { it.copy(weekNumber = w) }
    fun setType(t: String) = _state.update { it.copy(type = t) }

    private fun update(
        securityCorrect: Int = _state.value.securityCorrect,
        portugueseCorrect: Int = _state.value.portugueseCorrect,
        mathCorrect: Int = _state.value.mathCorrect
    ) {
        val status = evaluateCutoff(securityCorrect, portugueseCorrect, mathCorrect)
        _state.update { it.copy(securityCorrect = securityCorrect, portugueseCorrect = portugueseCorrect, mathCorrect = mathCorrect, cutoffStatus = status) }
    }

    fun save() {
        val s = _state.value
        viewModelScope.launch {
            val sim = Simulado(
                id = if (s.isNew) 0L else s.id,
                date = s.date,
                weekNumber = s.weekNumber,
                type = s.type,
                securityCorrect = s.securityCorrect,
                portugueseCorrect = s.portugueseCorrect,
                mathCorrect = s.mathCorrect,
                isPlanned = s.isPlanned
            )
            if (s.isNew) repository.insert(sim) else repository.update(sim)
            _state.update { it.copy(isSaved = true) }
        }
    }
}
