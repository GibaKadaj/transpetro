package com.prep.transpetro.ui.screens.blockcoverage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prep.transpetro.data.db.entity.BlockCoverage
import com.prep.transpetro.data.repository.BlockCoverageRepository
import com.prep.transpetro.domain.model.StudyConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class BlockCoverageUiItem(
    val code: String,
    val name: String,
    val subject: String,
    val firstStudied: String?,
    val lastStudied: String?,
    val timesStudied: Int,
    val confidence: Int
)

data class BlockCoverageUiState(
    val items: List<BlockCoverageUiItem> = emptyList()
)

@HiltViewModel
class BlockCoverageViewModel @Inject constructor(
    private val repository: BlockCoverageRepository
) : ViewModel() {

    val state = repository.getAllFlow().map { coverages ->
        val map = coverages.associateBy { it.blockCode }
        val items = StudyConstants.BLOCKS.map { block ->
            val cov = map[block.code]
            BlockCoverageUiItem(
                code = block.code,
                name = block.name,
                subject = block.subject,
                firstStudied = cov?.firstStudied,
                lastStudied = cov?.lastStudied,
                timesStudied = cov?.timesStudied ?: 0,
                confidence = cov?.confidence ?: 0
            )
        }
        BlockCoverageUiState(items = items)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BlockCoverageUiState())

    fun setConfidence(blockCode: String, level: Int) {
        viewModelScope.launch {
            val existing = repository.getByCode(blockCode)
            val today = LocalDate.now().toString()
            if (existing == null) {
                repository.upsert(
                    BlockCoverage(
                        blockCode = blockCode,
                        firstStudied = today,
                        lastStudied = today,
                        timesStudied = 1,
                        confidence = level
                    )
                )
            } else {
                repository.upsert(existing.copy(confidence = level, lastStudied = today))
            }
        }
    }
}
