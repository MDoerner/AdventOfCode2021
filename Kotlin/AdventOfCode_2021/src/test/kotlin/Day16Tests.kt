import adventOfCode2021.daySolutions.Day16
import adventOfCode2021.daySolutions.DaySolver
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day16Tests: DayTestBase() {
    override fun daySolverUnderTest(): DaySolver {
        return Day16()
    }

    private val part1TestSpecs = listOf(
        "D2FE28" to 6,
        "38006F45291200" to 9,
        "EE00D40C823060" to 14,
        "8A004A801A8002F478" to 16,
        "620080001611562C8802118E34" to 12,
        "C0015000016115A2E0802F182340" to 23,
        "A0016C880162017C3686B18A3D4780" to 31,
    )

    @TestFactory
    fun testPart1Examples() = part1TestSpecs
        .map { (message, expected) ->
            DynamicTest.dynamicTest("The message $message has a version sum of $expected") {
                testExampleInput( 1, message, expected.toString())
            }
        }

    @Test
    internal fun part1Solution() {
        val expectedResult = 847.toString()
        testOnDayInput(16, 1, expectedResult)
    }

    private val part2TestSpecs = listOf(
        "C200B40A82" to 3,
        "04005AC33890" to 54,
        "880086C3E88112" to 7,
        "CE00C43D881120" to 9,
        "D8005AC2A8F0" to 1,
        "F600BC2D8F" to 0,
        "9C005AC2F8F0" to 0,
        "9C0141080250320F1802104A08" to 1,
    )

    @TestFactory
    fun testPart2Examples() = part2TestSpecs
        .map { (message, expected) ->
            DynamicTest.dynamicTest("The message $message has a version sum of $expected") {
                testExampleInput( 2, message, expected.toString())
            }
        }

    @Test
    internal fun part2Solution() {
        val expectedResult = 333794664059UL.toString()
        testOnDayInput(16, 2, expectedResult)
    }
}