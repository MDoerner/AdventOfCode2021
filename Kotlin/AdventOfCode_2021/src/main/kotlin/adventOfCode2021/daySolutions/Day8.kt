package adventOfCode2021.daySolutions


typealias DigitSignal = Set<Char>

data class DisplaySpecification(val possiblePatterns: Set<DigitSignal>, val displayedNumber: List<DigitSignal>)

class Day8 : Day<List<DisplaySpecification>, Int> {
    override fun parseInput(input: String): List<DisplaySpecification> {
        return input.lines().mapNotNull(::parseDisplaySpecification)
    }

    private fun parseDisplaySpecification(text: String): DisplaySpecification? {
        val parts = text.split(" | ")
        if (parts.count() != 2) {
            return null
        }
        val possiblePatterns = parts[0]
            .split(' ')
            .map(String::toSet)
            .toSet()
        val displayedNumber = parts[1]
            .split(' ')
            .map(String::toSet)
        return DisplaySpecification(possiblePatterns, displayedNumber)
    }

    override fun solvePart1(input: List<DisplaySpecification>): Int {
        return input.sumOf { displaySpec ->
            displaySpec.displayedNumber
                .count { it.count() == 2 || it.count() == 3 || it.count() == 4 || it.count() == 7 }
        }
    }

    override fun solvePart2(input: List<DisplaySpecification>): Int {
        return input.map(::decodedDisplay).sum()
    }

    private fun decodedDisplay(spec: DisplaySpecification): Int {
        val decodedDigitSignals = digitSignals(spec.possiblePatterns) ?: return -1
        val digits = spec.displayedNumber.map { decodedDigitSignals.indexOf(it) }
        return toNumber(digits)
    }

    private fun digitSignals(possiblePatterns: Set<DigitSignal>): List<DigitSignal>? {
        val signalsBySegmentCount = possiblePatterns.groupBy(DigitSignal::count)

        val eightSignal = signalsBySegmentCount[7]?.singleOrNull() ?: return null
        val sevenSignal = signalsBySegmentCount[3]?.singleOrNull() ?: return null
        val oneSignal = signalsBySegmentCount[2]?.singleOrNull() ?: return null
        val fourSignal = signalsBySegmentCount[4]?.singleOrNull() ?: return null
        val fourArmSignal = fourSignal.subtract(oneSignal)

        val threeSignal = signalsBySegmentCount[5]?.singleOrNull { it.containsAll(oneSignal) } ?: return null
        val fiveSignal = signalsBySegmentCount[5]?.singleOrNull { it.containsAll(fourArmSignal) } ?: return null
        val twoSignal = signalsBySegmentCount[5]
            ?.subtract(setOf(threeSignal, fiveSignal))
            ?.singleOrNull()
            ?: return null

        val sixSignal = signalsBySegmentCount[6]?.singleOrNull { !it.containsAll(oneSignal) } ?: return null
        val zeroSignal = signalsBySegmentCount[6]?.singleOrNull { !it.containsAll(fourArmSignal) } ?: return null
        val nineSignal = signalsBySegmentCount[6]
            ?.subtract(setOf(sixSignal, zeroSignal))
            ?.singleOrNull()
            ?: return null

        return listOf(
            zeroSignal,
            oneSignal,
            twoSignal,
            threeSignal,
            fourSignal,
            fiveSignal,
            sixSignal,
            sevenSignal,
            eightSignal,
            nineSignal
        )
    }

    private fun toNumber(digits: List<Int>): Int = digits
        .fold(0) { acc, digit -> 10 * acc + digit}
}