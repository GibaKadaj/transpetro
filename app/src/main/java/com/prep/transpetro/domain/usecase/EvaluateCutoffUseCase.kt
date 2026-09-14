package com.prep.transpetro.domain.usecase

import com.prep.transpetro.domain.model.CutoffStatus
import com.prep.transpetro.domain.model.StudyConstants
import javax.inject.Inject

class EvaluateCutoffUseCase @Inject constructor() {

    fun execute(
        securityCorrect: Int,
        portugueseCorrect: Int,
        mathCorrect: Int
    ): CutoffStatus {
        if (portugueseCorrect == 0) return CutoffStatus.EliminatedZero("Português")
        if (mathCorrect == 0) return CutoffStatus.EliminatedZero("Matemática")
        if (securityCorrect < StudyConstants.MIN_SECURITY_PASS) return CutoffStatus.BelowSecurityMinimum
        val generalTotal = portugueseCorrect + mathCorrect
        if (generalTotal < StudyConstants.MIN_GENERAL_PASS) return CutoffStatus.BelowGeneralMinimum
        return CutoffStatus.PassesAll
    }
}
