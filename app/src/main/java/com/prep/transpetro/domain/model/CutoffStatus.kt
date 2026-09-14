package com.prep.transpetro.domain.model

sealed class CutoffStatus {
    object PassesAll : CutoffStatus()
    object BelowSecurityMinimum : CutoffStatus()
    object BelowGeneralMinimum : CutoffStatus()
    data class EliminatedZero(val subject: String) : CutoffStatus()
}
