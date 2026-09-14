package com.prep.transpetro.domain.usecase

import com.prep.transpetro.domain.model.CutoffStatus
import com.prep.transpetro.domain.model.StudyConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluateCutoffUseCaseTest {

    private val useCase = EvaluateCutoffUseCase()

    @Test
    fun `zero security score is eliminated`() {
        val result = useCase(
            securityCorrect = 0,
            portugueseCorrect = 8,
            mathCorrect = 8
        )
        assertTrue(result is CutoffStatus.EliminatedZero)
        assertEquals("Segurança", (result as CutoffStatus.EliminatedZero).subject)
    }

    @Test
    fun `zero portuguese score is eliminated`() {
        val result = useCase(
            securityCorrect = 30,
            portugueseCorrect = 0,
            mathCorrect = 8
        )
        assertTrue(result is CutoffStatus.EliminatedZero)
        assertEquals("Português", (result as CutoffStatus.EliminatedZero).subject)
    }

    @Test
    fun `zero math score is eliminated`() {
        val result = useCase(
            securityCorrect = 30,
            portugueseCorrect = 8,
            mathCorrect = 0
        )
        assertTrue(result is CutoffStatus.EliminatedZero)
        assertEquals("Matemática", (result as CutoffStatus.EliminatedZero).subject)
    }

    @Test
    fun `security below minimum fails security cutoff`() {
        val result = useCase(
            securityCorrect = StudyConstants.MIN_SECURITY_PASS - 1,
            portugueseCorrect = 8,
            mathCorrect = 8
        )
        assertTrue(result is CutoffStatus.BelowSecurityMinimum)
    }

    @Test
    fun `weighted score below minimum fails general cutoff`() {
        val result = useCase(
            securityCorrect = StudyConstants.MIN_SECURITY_PASS,
            portugueseCorrect = 1,
            mathCorrect = 1
        )
        assertTrue(result is CutoffStatus.BelowGeneralMinimum)
    }

    @Test
    fun `passing all cutoffs returns PassesAll`() {
        val result = useCase(
            securityCorrect = StudyConstants.MIN_SECURITY_PASS,
            portugueseCorrect = 7,
            mathCorrect = 7
        )
        assertTrue(result is CutoffStatus.PassesAll)
    }

    @Test
    fun `full marks passes all`() {
        val result = useCase(
            securityCorrect = StudyConstants.SECURITY_QUESTIONS,
            portugueseCorrect = StudyConstants.PORTUGUESE_QUESTIONS,
            mathCorrect = StudyConstants.MATH_QUESTIONS
        )
        assertTrue(result is CutoffStatus.PassesAll)
    }
}
