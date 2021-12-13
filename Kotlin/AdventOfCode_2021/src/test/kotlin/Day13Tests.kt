import adventOfCode2021.daySolutions.Day13
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class Day13Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day13()
    }

    private val part1ExampleInput =
        "6,10\n" +
        "0,14\n" +
        "9,10\n" +
        "0,3\n" +
        "10,4\n" +
        "4,11\n" +
        "6,0\n" +
        "6,12\n" +
        "4,1\n" +
        "0,13\n" +
        "10,12\n" +
        "3,4\n" +
        "3,0\n" +
        "8,4\n" +
        "1,10\n" +
        "2,14\n" +
        "8,10\n" +
        "9,0\n" +
        "\n" +
        "fold along y=7\n" +
        "fold along x=5"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 17.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Example2() {
        val input = part1ExampleInput
        val expectedResult = 16
        val solver = Day13()
        val parsedInput = solver.parseInput(input)
        val actualResult = solver.foldResult(parsedInput.dots, parsedInput.folds).count()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 842.toString()
        testOnDayInput(13, 1, expectedResult)
    }

    val part2ExampleResult =
        "#####\n" +
        "#...#\n" +
        "#...#\n" +
        "#...#\n" +
        "#####\n"

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = part2ExampleResult
        testExampleInput(2, input, expectedResult)
    }

    val part2Result =
        "###..####.#..#.###...##....##.####.#..#\n" +
        "#..#.#....#.#..#..#.#..#....#....#.#..#\n" +
        "###..###..##...#..#.#.......#...#..#..#\n" +
        "#..#.#....#.#..###..#.......#..#...#..#\n" +
        "#..#.#....#.#..#.#..#..#.#..#.#....#..#\n" +
        "###..#....#..#.#..#..##...##..####..##.\n"

    @Test
    internal fun part2Solution() {
        val expectedResult = part2Result
        testOnDayInput(13, 2, expectedResult)
    }
}