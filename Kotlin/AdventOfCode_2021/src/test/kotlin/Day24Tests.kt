import adventOfCode2021.daySolutions.Day24
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day24Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day24()
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 39999698799429UL.toString()
        testOnDayInput(24, 1, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 18116121134117UL.toString()
        testOnDayInput(24, 2, expectedResult)
    }
}