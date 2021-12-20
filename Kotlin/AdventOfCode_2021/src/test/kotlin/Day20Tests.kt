import adventOfCode2021.daySolutions.Day20
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.Test

class Day20Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day20()
    }

    private val part1ExampleInput =
        "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\n" +
                "\n" +
                "#..#.\n" +
                "#....\n" +
                "##..#\n" +
                "..#..\n" +
                "..###"

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 35.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 5259.toString()
        testOnDayInput(20, 1, expectedResult)
    }

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 3351.toString()
        testExampleInput(2, input, expectedResult)
    }

//    @Test
//    internal fun part2Solution() {
//        val expectedResult = 4676.toString()
//        testOnDayInput(20, 2, expectedResult)
//    }
}