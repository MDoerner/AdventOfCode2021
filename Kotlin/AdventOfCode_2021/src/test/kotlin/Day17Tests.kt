import adventOfCode2021.daySolutions.Day17
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day17Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day17()
    }

    private val part1ExampleInput =
        "target area: x=20..30, y=-10..-5"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 45.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 4186.toString()
        testOnDayInput(17, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 112.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 2709.toString()
        testOnDayInput(17, 2, expectedResult)
    }
}