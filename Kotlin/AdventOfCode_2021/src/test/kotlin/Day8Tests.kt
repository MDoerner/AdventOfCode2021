import adventOfCode2021.daySolutions.Day8
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day8Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day8()
    }

    private val part1ExampleInput =
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe\n" +
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc\n" +
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg\n" +
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb\n" +
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea\n" +
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb\n" +
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe\n" +
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef\n" +
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb\n" +
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 26.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 264.toString()
        testOnDayInput(8, 1, expectedResult)
    }

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 61229.toString()
        testExampleInput(2, input, expectedResult)
    }

    private val individualSpecsTestData =
        (listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf") + part2ExampleInput.lines())
            .zip(listOf(5353, 8394, 9781, 1197, 9361, 4873, 8418, 4548, 1625, 8717, 4315))

    @TestFactory
    fun testIndividualSpecDecodings() = individualSpecsTestData
        .map { (input, expected) ->
            DynamicTest.dynamicTest("Decoding display '$input' results in $expected") {
                testExampleInput(2, input, expected.toString())
            }
        }

    @Test
    internal fun part2Solution() {
        val expectedResult = 1063760.toString()
        testOnDayInput(8, 2, expectedResult)
    }
}