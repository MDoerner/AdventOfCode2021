import adventOfCode2021.daySolutions.Day11
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day11Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day11()
    }

    private val part1ExampleInput =
        "5483143223\n" +
        "2745854711\n" +
        "5264556173\n" +
        "6141336146\n" +
        "6357385478\n" +
        "4167524645\n" +
        "2176841721\n" +
        "6882881134\n" +
        "4846848554\n" +
        "5283751526"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 1656.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 1644.toString()
        testOnDayInput(11, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 195.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 229.toString()
        testOnDayInput(11, 2, expectedResult)
    }
}