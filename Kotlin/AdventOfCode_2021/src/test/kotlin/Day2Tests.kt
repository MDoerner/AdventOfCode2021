import adventOfCode2021.daySolutions.Day2
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day2Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day2()
    }

    private val part1ExampleInput =
        "forward 5\n" +
        "down 5\n" +
        "forward 8\n" +
        "up 3\n" +
        "down 8\n" +
        "forward 2"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 150.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 2102357.toString()
        testOnDayInput(2, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 900.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 2101031224.toString()
        testOnDayInput(2, 2, expectedResult)
    }
}