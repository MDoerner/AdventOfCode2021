import adventOfCode2021.daySolutions.Day7
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day7Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day7()
    }

    private val part1ExampleInput = "16,1,2,0,4,2,7,1,2,14"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 37.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 342730.toString()
        testOnDayInput(7, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 168.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 92335207.toString()
        testOnDayInput(7, 2, expectedResult)
    }
}