import adventOfCode2021.daySolutions.Day5
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day5Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day5()
    }

    private val part1ExampleInput =
        "0,9 -> 5,9\n" +
        "8,0 -> 0,8\n" +
        "9,4 -> 3,4\n" +
        "2,2 -> 2,1\n" +
        "7,0 -> 7,4\n" +
        "6,4 -> 2,0\n" +
        "0,9 -> 2,9\n" +
        "3,4 -> 1,4\n" +
        "0,0 -> 8,8\n" +
        "5,5 -> 8,2"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 5.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 5124.toString()
        testOnDayInput(5, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 12.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 19771.toString()
        testOnDayInput(5, 2, expectedResult)
    }
}