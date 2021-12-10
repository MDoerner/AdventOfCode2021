import adventOfCode2021.daySolutions.Day10
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day10Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day10()
    }

    private val part1ExampleInput =
        "[({(<(())[]>[[{[]{<()<>>\n" +
        "[(()[<>])]({[<{<<[]>>(\n" +
        "{([(<{}[<>[]}>{[]{[(<()>\n" +
        "(((({<>}<{<{<>}{[]{[]{}\n" +
        "[[<[([]))<([[{}[[()]]]\n" +
        "[{[{({}]{}}([{[{{{}}([]\n" +
        "{<[[]]>}<{[{[{[]{()[[[]\n" +
        "[<(<(<(<{}))><([]([]()\n" +
        "<{([([[(<>()){}]>(<<{{\n" +
        "<{([{{}}[<[[[<>{}]]]>[]]"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 26397.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 392421.toString()
        testOnDayInput(10, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 288957.toString()
        testExampleInput(2, input, expectedResult)
    }

    private val individualLinesTestData =
        part2ExampleInput.lines()
            .zip(listOf(288957, 5566, 0, 1480781, 0, 0, 995444, 0, 0, 294))

    @TestFactory
    fun testIndividualSpecDecodings() = individualLinesTestData
        .map { (input, expected) ->
            DynamicTest.dynamicTest("Score for completing line '$input' is $expected") {
                testExampleInput(2, input, expected.toString())
            }
        }

    @Test
    internal fun part2Solution() {
        val expectedResult = 2769449099UL.toString()
        testOnDayInput(10, 2, expectedResult)
    }
}