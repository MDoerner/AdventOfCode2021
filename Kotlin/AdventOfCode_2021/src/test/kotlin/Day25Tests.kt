import adventOfCode2021.daySolutions.Day25
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day25Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day25()
    }

    private val part1ExampleInput =
        "v...>>.vv>\n" +
        ".vv>>.vv..\n" +
        ">>.>v>...v\n" +
        ">>v>>.>.v.\n" +
        "v>v.vv.v..\n" +
        ">.>>..v...\n" +
        ".vv..>.>v.\n" +
        "v.v..>>v.v\n" +
        "....v..v.>"

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 58.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 305.toString()
        testOnDayInput(25, 1, expectedResult)
    }
}