package com.prep.transpetro.ui.screens.errorlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.ErrorLogEntry
import com.prep.transpetro.data.repository.ErrorLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ErrorLogUiState(
    val entries: List<ErrorLogEntry> = emptyList(),
    val query: String = "",
    val filterBlock: String = "",
    val filterReason: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class ErrorLogViewModel @Inject constructor(
    private val repository: ErrorLogRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _filterBlock = MutableStateFlow("")
    private val _filterReason = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)

    val state = combine(
        repository.getAllFlow(),
        _query,
        _filterBlock,
        _filterReason
    ) { all, query, block, reason ->
        _isLoading.value = false
        val filtered = all.filter { entry ->
            (query.isBlank() || entry.topicNorm.contains(query, true) ||
                entry.triggerWord.contains(query, true) ||
                entry.correctionLine.contains(query, true)) &&
            (block.isBlank() || entry.blockCode == block) &&
            (reason.isBlank() || entry.errorReason == reason)
        }.sortedByDescending { it.date }
        ErrorLogUiState(entries = filtered, query = query, filterBlock = block, filterReason = reason, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ErrorLogUiState())

    fun setQuery(q: String) = _query.update { q }
    fun setFilterBlock(b: String) = _filterBlock.update { b }
    fun setFilterReason(r: String) = _filterReason.update { r }
    fun delete(entry: ErrorLogEntry) { viewModelScope.launch { repository.delete(entry) } }
}
