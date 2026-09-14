package com.prep.transpetro.domain.usecase

import com.prep.transpetro.domain.model.StudyConstants
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CheckBlockAlertUseCaseTest {

    private val useCase = CheckBlockAlertUseCase()
    private val cutoff = LocalDate.parse(
        StudyConstants.NO_NEW_CONTENT_AFTER,
        DateTimeFormatter.ISO_LOCAL_DATE
    )

    @Test
    fun `not started block before cutoff does not alert`() {
        val before = cutoff.minusDays(1)
        assertFalse(useCase.shouldAlert("NAO_INICIADO", before))
    }

    @Test
    fun `not started block after cutoff alerts`() {
        val after = cutoff.plusDays(1)
        assertTrue(useCase.shouldAlert("NAO_INICIADO", after))
    }

    @Test
    fun `started block after cutoff does not alert`() {
        val after = cutoff.plusDays(1)
        assertFalse(useCase.shouldAlert("EM_REVISAO", after))
    }

    @Test
    fun `isNewContentBlocked returns false before cutoff`() {
        assertFalse(useCase.isNewContentBlocked(cutoff.minusDays(1)))
    }

    @Test
    fun `isNewContentBlocked returns true after cutoff`() {
        assertTrue(useCase.isNewContentBlocked(cutoff.plusDays(1)))
    }

    @Test
    fun `isNewContentBlocked returns false on cutoff day itself`() {
        assertFalse(useCase.isNewContentBlocked(cutoff))
    }
}
