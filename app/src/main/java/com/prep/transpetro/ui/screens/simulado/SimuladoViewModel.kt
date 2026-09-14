package com.prep.transpetro.ui.screens.simulado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.Simulado
import com.prep.transpetro.data.repository.SimuladoRepository
import com.prep.transpetro.domain.model.CutoffStatus
import com.prep.transpetro.domain.usecase.EvaluateCutoffUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SimuladoUiState(
    val simulados: List<Simulado> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SimuladoViewModel @Inject constructor(
    private val repository: SimuladoRepository,
    private val evaluateCutoff: EvaluateCutoffUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SimuladoUiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SimuladoUiState())

    init {
        viewModelScope.launch {
            repository.getAllFlow().collect { list ->
                _state.update { it.copy(simulados = list.sortedByDescending { s -> s.date }, isLoading = false) }
            }
        }
    }

    fun cutoffFor(sim: Simulado): CutoffStatus =
        evaluateCutoff(sim.securityCorrect, sim.portugueseCorrect, sim.mathCorrect)

    fun delete(sim: Simulado) {
        viewModelScope.launch { repository.delete(sim) }
    }
}
