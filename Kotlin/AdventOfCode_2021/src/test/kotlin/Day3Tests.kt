import adventOfCode2021.daySolutions.Day3
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day3Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day3()
    }

    private val part1ExampleInput =
        "00100\n" +
        "11110\n" +
        "10110\n" +
        "10111\n" +
        "10101\n" +
        "01111\n" +
        "00111\n" +
        "11100\n" +
        "10000\n" +
        "11001\n" +
        "00010\n" +
        "01010"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 198.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 3374136.toString()
        testOnDayInput(3, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 230.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 4432698.toString()
        testOnDayInput(3, 2, expectedResult)
    }
}