import adventOfCode2021.daySolutions.Day9
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day9Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day9()
    }

    private val part1ExampleInput =
        "2199943210\n" +
        "3987894921\n" +
        "9856789892\n" +
        "8767896789\n" +
        "9899965678"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 15.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 425.toString()
        testOnDayInput(9, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 1134.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 1135260.toString()
        testOnDayInput(9, 2, expectedResult)
    }
}