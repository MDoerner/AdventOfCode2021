import adventOfCode2021.daySolutions.Day18
import adventOfCode2021.daySolutions.DaySolver
import adventOfCode2021.daySolutions.SnailFishNumber
import adventOfCode2021.daySolutions.sum
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class Day18Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day18()
    }

    private val part1OperationTestSpecs = listOf(
        "[1,2]" to "[1,2]",
        "[[1,2],3]" to "[[1,2],3]",
        "[9,[8,7]]" to "[9,[8,7]]",
        "[[1,9],[8,5]]" to "[[1,9],[8,5]]",
        "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]" to "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
        "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]" to "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
        "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]" to "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
        "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "10" to "[5,5]",
        "11" to "[5,6]",
        "12" to "[6,6]",
        "[[[[4,3],4],4],[7,[[8,4],9]]]\n" +
            "[1,1]" to "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        "[1,1]\n" +
            "[2,2]\n" +
            "[3,3]\n" +
            "[4,4]" to "[[[[1,1],[2,2]],[3,3]],[4,4]]",
        "[1,1]\n" +
                "[2,2]\n" +
                "[3,3]\n" +
                "[4,4]\n" +
                "[5,5]" to "[[[[3,0],[5,3]],[4,4]],[5,5]]",
        "[1,1]\n" +
            "[2,2]\n" +
            "[3,3]\n" +
            "[4,4]\n" +
            "[5,5]\n" +
            "[6,6]" to "[[[[5,0],[7,4]],[5,5]],[6,6]]",
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\n" +
            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]\n" +
            "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]\n" +
            "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]\n" +
            "[7,[5,[[3,8],[1,4]]]]\n" +
            "[[2,[2,2]],[8,[8,1]]]\n" +
            "[2,9]\n" +
            "[1,[[[9,3],9],[[9,0],[0,7]]]]\n" +
            "[[[5,[7,4]],7],1]\n" +
            "[[[[4,2],2],6],[8,7]]" to "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
            "[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
            "[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
            "[[9,3],[[9,9],[6,[4,9]]]]\n" +
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]" to "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]",
    )

    private fun testOperations(input: String, expectedResult: String){
        val solver = Day18()
        val numbers = solver.parseInput(input).map(SnailFishNumber::reduce)
        val actualResult = numbers.sum().toString()
        assertEquals(expectedResult, actualResult)
    }

    @TestFactory
    fun testPart1Operations() = part1OperationTestSpecs
        .map { (numberList, expected) ->
            val oneLineNumbers = numberList.lines().joinToString("; ")
            val numbersString = if (oneLineNumbers.length <= 12) oneLineNumbers else oneLineNumbers.take(10) + ".."
            DynamicTest.dynamicTest("Summing up the list of ${numberList.lines().count()} lines starting with $numbersString results in $expected") {
                testOperations( numberList, expected)
            }
        }

    private val part1MagnitudeTestSpecs = listOf(
        "[9,1]" to 29,
        "[1,9]" to 21,
        "[[9,1],[1,9]]" to 129,
        "[[1,2],[[3,4],5]]" to 143,
        "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" to 1384,
        "[[[[1,1],[2,2]],[3,3]],[4,4]]" to 445,
        "[[[[3,0],[5,3]],[4,4]],[5,5]]" to 791,
        "[[[[5,0],[7,4]],[5,5]],[6,6]]" to 1137,
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" to 3488,
        "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]" to 4140,
    )

    @TestFactory
    fun testPart1MagnitudeExamples() = part1MagnitudeTestSpecs
        .map { (number, expected) ->
            DynamicTest.dynamicTest("The magnitude of $number is $expected") {
                testExampleInput( 1, number, expected.toString())
            }
        }

    private val part1ExampleInput =
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
            "[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
            "[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
            "[[9,3],[[9,9],[6,[4,9]]]]\n" +
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"

    @Test
    internal fun part1Example() {
        val input = part1ExampleInput
        val expectedResult = 4140.toString()
        testExampleInput(1, input, expectedResult)
    }

    @Test
    internal fun part1Solution() {
        val expectedResult = 4480.toString()
        testOnDayInput(18, 1, expectedResult)
    }

    private val part2ExampleInput = part1ExampleInput

    @Test
    internal fun part2Example() {
        val input = part2ExampleInput
        val expectedResult = 3993.toString()
        testExampleInput(2, input, expectedResult)
    }

    @Test
    internal fun part2Solution() {
        val expectedResult = 4676.toString()
        testOnDayInput(18, 2, expectedResult)
    }
}