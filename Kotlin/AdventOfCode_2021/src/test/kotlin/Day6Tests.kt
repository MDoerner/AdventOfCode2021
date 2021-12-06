import adventOfCode2021.daySolutions.Day6
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day6Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day6()
    }

    private val part1ExampleInput = "3,4,3,1,2"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 5934.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 362666.toString()
        testOnDayInput(6, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 26984457539UL.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 1640526601595UL.toString()
        testOnDayInput(6, 2, expectedResult)
    }
}