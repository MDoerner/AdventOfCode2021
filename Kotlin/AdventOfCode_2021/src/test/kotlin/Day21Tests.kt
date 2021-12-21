import adventOfCode2021.daySolutions.Day21
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day21Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day21()
    }

    private val part1ExampleInput =
        "Player 1 starting position: 4\n" +
                "Player 2 starting position: 8"

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 739785.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 929625.toString()
        testOnDayInput(21, 1, expectedResult)
    }

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 444356092776315UL.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 175731756652760UL.toString()
        testOnDayInput(21, 2, expectedResult)
    }
}