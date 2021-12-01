import daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day1Tests: DayTestBase() {

    override fun DaySolverUnderTest(): DaySolver {
        return daySolutions.Day1()
    }

    private val part1ExampleInput =
        "199\n" +
        "200\n" +
        "208\n" +
        "210\n" +
        "200\n" +
        "207\n" +
        "240\n" +
        "269\n" +
        "260\n" +
        "263"

    private val part2ExampleInput =
        "607\n" +
        "618\n" +
        "618\n" +
        "617\n" +
        "647\n" +
        "716\n" +
        "769\n" +
        "792\n"

    @Test
    internal fun Part1Example() {
        val input = part1ExampleInput
        val expectedResult = 7.toString()
        TestExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun Part1Solution() {
        val expectedResult = 1502.toString()
        TestOnDayInput(1, 1, expectedResult)
    }

    @Test
    internal fun Part2Example() {
        val input = part2ExampleInput
        val expectedResult = 5.toString()
        TestExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun Part2Solution() {
        val expectedResult = 1538.toString()
        TestOnDayInput(1, 2, expectedResult)
    }
}