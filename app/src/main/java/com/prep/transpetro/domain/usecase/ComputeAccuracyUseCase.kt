package com.prep.transpetro.domain.usecase

import javax.inject.Inject

class ComputeAccuracyUseCase @Inject constructor() {

    fun execute(correct: Int, total: Int): Float {
        if (total == 0) return 0f
        return correct.toFloat() / total.toFloat() * 100f
    }
}
