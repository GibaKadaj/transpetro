package com.prep.transpetro.domain.usecase

import com.prep.transpetro.domain.model.StudyConstants
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CheckBlockAlertUseCase @Inject constructor() {

    fun shouldAlert(blockStatus: String, today: LocalDate): Boolean {
        val cutoff = LocalDate.parse(
            StudyConstants.NO_NEW_CONTENT_AFTER,
            DateTimeFormatter.ISO_LOCAL_DATE
        )
        return today.isAfter(cutoff) && blockStatus == "NAO_INICIADO"
    }

    fun isNewContentBlocked(targetDate: LocalDate): Boolean {
        val cutoff = LocalDate.parse(
            StudyConstants.NO_NEW_CONTENT_AFTER,
            DateTimeFormatter.ISO_LOCAL_DATE
        )
        return targetDate.isAfter(cutoff)
    }
}
