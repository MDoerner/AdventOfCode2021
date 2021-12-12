import adventOfCode2021.daySolutions.Day12
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day12Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day12()
    }

    private val part1ExampleInputs = listOf(
        "start-A\n" +
        "start-b\n" +
        "A-c\n" +
        "A-b\n" +
        "b-d\n" +
        "A-end\n" +
        "b-end",
        "dc-end\n" +
        "HN-start\n" +
        "start-kj\n" +
        "dc-start\n" +
        "dc-HN\n" +
        "LN-dc\n" +
        "HN-end\n" +
        "kj-sa\n" +
        "kj-HN\n" +
        "kj-dc",
        "fs-end\n" +
        "he-DX\n" +
        "fs-he\n" +
        "start-DX\n" +
        "pj-DX\n" +
        "end-zg\n" +
        "zg-sl\n" +
        "zg-pj\n" +
        "pj-he\n" +
        "RW-he\n" +
        "fs-DX\n" +
        "pj-RW\n" +
        "zg-RW\n" +
        "start-pj\n" +
        "he-WI\n" +
        "zg-he\n" +
        "pj-fs\n" +
        "start-RW",
    )

    private val part2ExampleInputs = part1ExampleInputs

    private val partOneIndividualTestData = part1ExampleInputs
        .zip(listOf(10, 19, 226))

    @TestFactory
    fun testIndividualPart1Examples() = partOneIndividualTestData
        .mapIndexed { index, (input, expected) ->
            DynamicTest.dynamicTest("Example cave $index contains $expected paths without small cave revisits") {
                testExampleInput(1, input, expected.toString())
            }
        }

    @Test
    internal fun part1Solution() {
        val expectedResult = 4411.toString()
        testOnDayInput(12, 1, expectedResult)
    }

    private val partTwoIndividualTestData = part2ExampleInputs
        .zip(listOf(36, 103, 3509))

    @TestFactory
    fun testIndividualPart2Examples() = partTwoIndividualTestData
        .mapIndexed { index, (input, expected) ->
            DynamicTest.dynamicTest("Example cave $index contains $expected paths with at most one small cave revisit") {
                testExampleInput(2, input, expected.toString())
            }
        }

    @Test
    internal fun part2Solution() {
        val expectedResult = 136767.toString()
        testOnDayInput(12, 2, expectedResult)
    }
}