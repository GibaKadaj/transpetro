package com.prep.transpetro.ui.screens.studyplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.DailyPlan
import com.prep.transpetro.data.repository.DailyPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class StudyPlanUiState(
    val plans: List<DailyPlan> = emptyList(),
    val editingDate: String? = null,
    val editBlocks: String = "",
    val editHours: Float = 2f,
    val editNotes: String = ""
)

@HiltViewModel
class StudyPlanViewModel @Inject constructor(
    private val repository: DailyPlanRepository
) : ViewModel() {

    private val _editing = MutableStateFlow<String?>(null)
    private val _editBlocks = MutableStateFlow("")
    private val _editHours = MutableStateFlow(2f)
    private val _editNotes = MutableStateFlow("")

    val state = combine(
        repository.getUpcomingFlow(LocalDate.now().toString()),
        _editing,
        _editBlocks,
        _editHours,
        _editNotes
    ) { plans, editing, blocks, hours, notes ->
        StudyPlanUiState(
            plans = plans,
            editingDate = editing,
            editBlocks = blocks,
            editHours = hours,
            editNotes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudyPlanUiState())

    fun startEdit(plan: DailyPlan) {
        _editing.value = plan.date
        _editBlocks.value = plan.plannedBlocks
        _editHours.value = plan.plannedHours
        _editNotes.value = plan.notes
    }

    fun startNew(date: String) {
        _editing.value = date
        _editBlocks.value = ""
        _editHours.value = 2f
        _editNotes.value = ""
    }

    fun setBlocks(v: String) = _editBlocks.update { v }
    fun setHours(v: Float) = _editHours.update { v }
    fun setNotes(v: String) = _editNotes.update { v }

    fun save() {
        val date = _editing.value ?: return
        viewModelScope.launch {
            repository.upsert(
                DailyPlan(
                    date = date,
                    plannedBlocks = _editBlocks.value,
                    plannedHours = _editHours.value,
                    notes = _editNotes.value
                )
            )
            _editing.value = null
        }
    }

    fun cancelEdit() { _editing.value = null }
}
