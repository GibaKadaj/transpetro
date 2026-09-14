package com.prep.transpetro.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class ComputeAccuracyUseCaseTest {

    private val useCase = ComputeAccuracyUseCase()

    @Test
    fun `zero total returns zero accuracy`() {
        assertEquals(0f, useCase.execute(correct = 5, total = 0), 0.001f)
    }

    @Test
    fun `zero correct returns zero accuracy`() {
        assertEquals(0f, useCase.execute(correct = 0, total = 10), 0.001f)
    }

    @Test
    fun `full score returns 100`() {
        assertEquals(100f, useCase.execute(correct = 10, total = 10), 0.001f)
    }

    @Test
    fun `half correct returns 50`() {
        assertEquals(50f, useCase.execute(correct = 5, total = 10), 0.001f)
    }

    @Test
    fun `non-integer percentage is computed correctly`() {
        assertEquals(66.667f, useCase.execute(correct = 2, total = 3), 0.01f)
    }
}
