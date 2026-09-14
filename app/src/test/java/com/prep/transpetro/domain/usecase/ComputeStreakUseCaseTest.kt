package com.prep.transpetro.domain.usecase

import com.prep.transpetro.domain.model.StreakStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ComputeStreakUseCaseTest {

    private val useCase = ComputeStreakUseCase()

    private fun date(daysAgo: Int) = LocalDate.now().minusDays(daysAgo.toLong()).toString()

    @Test
    fun `empty list returns zero streak`() {
        val result = useCase(emptyList())
        assertEquals(StreakStatus(current = 0, best = 0), result)
    }

    @Test
    fun `single today entry returns streak of 1`() {
        val result = useCase(listOf(date(0)))
        assertEquals(1, result.current)
    }

    @Test
    fun `consecutive days produce correct streak`() {
        val dates = listOf(date(0), date(1), date(2), date(3))
        val result = useCase(dates)
        assertEquals(4, result.current)
    }

    @Test
    fun `gap resets streak`() {
        val dates = listOf(date(0), date(1), date(3), date(4))
        val result = useCase(dates)
        assertEquals(2, result.current)
    }

    @Test
    fun `best streak is preserved across gap`() {
        val dates = listOf(date(0), date(1), date(3), date(4), date(5), date(6))
        val result = useCase(dates)
        assertEquals(2, result.current)
        assertEquals(4, result.best)
    }

    @Test
    fun `duplicate dates do not inflate streak`() {
        val dates = listOf(date(0), date(0), date(1), date(1))
        val result = useCase(dates)
        assertEquals(2, result.current)
    }
}
