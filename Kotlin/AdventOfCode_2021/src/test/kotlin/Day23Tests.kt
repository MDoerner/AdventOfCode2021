import adventOfCode2021.daySolutions.Day23
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day23Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day23()
    }

    private val part1ExampleInput =
        "#############\n" +
        "#...........#\n" +
        "###B#C#B#D###\n" +
        "  #A#D#C#A#\n" +
        "  #########"

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 12521.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 13556.toString()
        testOnDayInput(23, 1, expectedResult)
    }

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 44169.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 54200.toString()
        testOnDayInput(23, 2, expectedResult)
    }
}