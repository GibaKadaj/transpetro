package com.prep.transpetro.ui.screens.errorlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.ErrorLogEntry
import com.prep.transpetro.data.repository.ErrorLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ErrorLogDetailUiState(
    val id: Long = 0L,
    val date: String = LocalDate.now().toString(),
    val blockCode: String = "",
    val topicNorm: String = "",
    val myAnswer: String = "",
    val correctAnswer: String = "",
    val errorReason: String = "DESCONHECIMENTO",
    val triggerWord: String = "",
    val correctionLine: String = "",
    val isSaved: Boolean = false,
    val isNew: Boolean = true
)

@HiltViewModel
class ErrorLogDetailViewModel @Inject constructor(
    private val repository: ErrorLogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ErrorLogDetailUiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ErrorLogDetailUiState())

    fun load(id: Long) {
        if (id <= 0L) return
        viewModelScope.launch {
            val entry = repository.getById(id) ?: return@launch
            _state.update {
                it.copy(
                    id = entry.id,
                    date = entry.date,
                    blockCode = entry.blockCode,
                    topicNorm = entry.topicNorm,
                    myAnswer = entry.myAnswer,
                    correctAnswer = entry.correctAnswer,
                    errorReason = entry.errorReason,
                    triggerWord = entry.triggerWord,
                    correctionLine = entry.correctionLine,
                    isNew = false
                )
            }
        }
    }

    fun setBlockCode(v: String) = _state.update { it.copy(blockCode = v) }
    fun setTopicNorm(v: String) = _state.update { it.copy(topicNorm = v) }
    fun setMyAnswer(v: String) = _state.update { it.copy(myAnswer = v) }
    fun setCorrectAnswer(v: String) = _state.update { it.copy(correctAnswer = v) }
    fun setErrorReason(v: String) = _state.update { it.copy(errorReason = v) }
    fun setTriggerWord(v: String) = _state.update { it.copy(triggerWord = v) }
    fun setCorrectionLine(v: String) = _state.update { it.copy(correctionLine = v) }

    fun save() {
        val s = _state.value
        viewModelScope.launch {
            val entry = ErrorLogEntry(
                id = if (s.isNew) 0L else s.id,
                date = s.date,
                blockCode = s.blockCode,
                topicNorm = s.topicNorm,
                myAnswer = s.myAnswer,
                correctAnswer = s.correctAnswer,
                errorReason = s.errorReason,
                triggerWord = s.triggerWord,
                correctionLine = s.correctionLine
            )
            if (s.isNew) repository.insert(entry) else repository.update(entry)
            _state.update { it.copy(isSaved = true) }
        }
    }
}
