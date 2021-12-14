import adventOfCode2021.daySolutions.Day14
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class Day14Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day14()
    }

    private val part1ExampleInput =
        "NNCB\n" +
        "\n" +
        "CH -> B\n" +
        "HH -> N\n" +
        "CB -> H\n" +
        "NH -> C\n" +
        "HB -> C\n" +
        "HC -> B\n" +
        "HN -> C\n" +
        "NN -> C\n" +
        "BH -> H\n" +
        "NC -> B\n" +
        "NB -> B\n" +
        "BN -> B\n" +
        "BB -> N\n" +
        "BC -> B\n" +
        "CC -> N\n" +
        "CN -> C\n"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 1588.toString()
        testExampleInput(1, input, expectedResult)
    }

    private fun testExamplePatternCount(selector: (Char) -> Boolean, steps: Int, expectedResult: Int){
        val input = part1ExampleInput
        val solver = Day14()
        val parsedInput = solver.parseInput(input)
        val actualResult = solver.performReplacements(parsedInput.template, parsedInput.rules, steps).count(selector)
        assertEquals(expectedResult, actualResult)
    }

    private val individualTotalCountInputs = listOf(
        -42 to 4,
        0 to 4,
        1 to 7,
        2 to 13,
        3 to 25,
        4 to 49,
        5 to 97,
        10 to 3073,
    )

    @TestFactory
    fun testIndividualPart1ExampleTotalCounts() = individualTotalCountInputs
        .map { (steps, expected) ->
            DynamicTest.dynamicTest("After $steps steps, the polymer contains $expected elements") {
                testExamplePatternCount( { true }, steps, expected)
            }
        }

    private val individualElementCountInputs = listOf(
        Triple(-42, 'N', 2),
        Triple(-42, 'C', 1),
        Triple(-42, 'B', 1),
        Triple(-42, 'H', 0),
        Triple(0, 'N', 2),
        Triple(0, 'C', 1),
        Triple(0, 'B', 1),
        Triple(0, 'H', 0),
        Triple(1, 'N', 2),
        Triple(1, 'C', 2),
        Triple(1, 'B', 2),
        Triple(1, 'H', 1),
        Triple(2, 'N', 2),
        Triple(2, 'C', 4),
        Triple(2, 'B', 6),
        Triple(2, 'H', 1),
        Triple(3, 'N', 5),
        Triple(3, 'C', 5),
        Triple(3, 'B', 11),
        Triple(3, 'H', 4),
        Triple(4, 'N', 11),
        Triple(4, 'C', 10),
        Triple(4, 'B', 23),
        Triple(4, 'H', 5),
        Triple(10, 'N', 865),
        Triple(10, 'C', 298),
        Triple(10, 'B', 1749),
        Triple(10, 'H', 161),
    )

    @TestFactory
    fun testIndividualPart1ExampleElementCounts() = individualElementCountInputs
        .map { (steps, element, expected) ->
            DynamicTest.dynamicTest("After $steps steps, the polymer contains the element '$element' $expected times") {
                testExamplePatternCount( { it == element }, steps, expected)
            }
        }

    private fun testExamplePattern(selector: (Char) -> Boolean, steps: Int, expectedResult: String){
        val input = part1ExampleInput
        val solver = Day14()
        val parsedInput = solver.parseInput(input)
        val actualResult = solver.performReplacements(parsedInput.template, parsedInput.rules, steps)
        assertEquals(expectedResult, actualResult)
    }

    private val individualPatternInputs = listOf(
        -42 to "NNCB",
        0 to "NNCB",
        1 to "NCNBCHB",
        2 to "NBCCNBBBCBHCB",
        3 to "NBBBCNCCNBBNBNBBCHBHHBCHB",
        4 to "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB",
    )

    @TestFactory
    fun testIndividualPart1Examples() = individualPatternInputs
        .map { (steps, expected) ->
            DynamicTest.dynamicTest("After $steps steps, the polymer is $expected") {
                testExamplePattern( { true }, steps, expected)
            }
        }

    @Test
    internal fun part1Solution() {
        val expectedResult = 2915.toString()
        testOnDayInput(14, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 2188189693529UL.toString()
        testExampleInput(2, input, expectedResult)
    }

    private fun testEnhancedExamplePatternCount(selector: (Char) -> Boolean, steps: Int, expectedResult: ULong){
        val input = part1ExampleInput
        val solver = Day14()
        val parsedInput = solver.parseInput(input)
        val elementCounts = solver.elementCounts(parsedInput, steps)
        val actualResult = elementCounts.asSequence()
            .filter { selector(it.key) }
            .sumOf { it.value }
        assertEquals(expectedResult, actualResult)
    }

    @TestFactory
    fun testIndividualPart2ExampleElementCounts() = individualElementCountInputs
        .map { (steps, element, expected) ->
            DynamicTest.dynamicTest("After $steps steps, the polymer contains the element '$element' $expected times") {
                testEnhancedExamplePatternCount( { it == element }, steps, expected.toULong())
            }
        }

    @TestFactory
    fun testIndividualPart2ExampleTotalCounts() = individualTotalCountInputs
        .map { (steps, expected) ->
            DynamicTest.dynamicTest("After $steps steps, the polymer contains $expected elements") {
                testEnhancedExamplePatternCount( { true }, steps, expected.toULong())
            }
        }

    @Test
    internal fun part2Solution() {
        val expectedResult = 3353146900153UL.toString()
        testOnDayInput(14, 2, expectedResult)
    }
}