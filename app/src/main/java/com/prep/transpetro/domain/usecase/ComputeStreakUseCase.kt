package com.prep.transpetro.domain.usecase

import com.prep.transpetro.domain.model.StreakStatus
import com.prep.transpetro.domain.model.StudyConstants
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ComputeStreakUseCase @Inject constructor() {

    fun execute(studiedDates: Set<String>, today: LocalDate): StreakStatus {
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE
        val start = LocalDate.parse(StudyConstants.PREP_START_DATE, fmt)
        val exam  = LocalDate.parse(StudyConstants.EXAM_DATE, fmt)

        val allPastDays = generateSequence(start) { it.plusDays(1) }
            .takeWhile { !it.isAfter(today.coerceAtMost(exam)) }
            .map { it.format(fmt) }
            .toList()

        val studied = studiedDates
        val lost = allPastDays.filter { it !in studied }

        val currentStreak = allPastDays.reversed()
            .takeWhile { it in studied }
            .size

        var longest = 0
        var running = 0
        for (d in allPastDays) {
            if (d in studied) {
                running++
                if (running > longest) longest = running
            } else {
                running = 0
            }
        }

        val consistencyPct = if (allPastDays.isEmpty()) 0f
        else studied.count { it in allPastDays }.toFloat() / allPastDays.size * 100f

        val hasConsecutiveMissAlert = run {
            val pastOnly = allPastDays.filter { it < today.format(fmt) }
            if (pastOnly.size < 2) false
            else {
                var consecutive = 0
                for (d in pastOnly.reversed()) {
                    if (d !in studied) consecutive++ else break
                    if (consecutive >= 2) return@run true
                }
                false
            }
        }

        return StreakStatus(
            studiedDates = studied,
            currentStreak = currentStreak,
            longestStreak = longest,
            totalStudied = studied.count { it in allPastDays },
            totalLost = lost.size,
            consistencyPct = consistencyPct,
            hasConsecutiveMissAlert = hasConsecutiveMissAlert
        )
    }
}
