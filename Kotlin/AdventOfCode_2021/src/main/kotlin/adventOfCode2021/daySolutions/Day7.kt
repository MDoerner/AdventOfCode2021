package adventOfCode2021.daySolutions

import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor


class Day7 : Day<List<Int>, Int> {

    override fun parseInput(input: String): List<Int>{
        return input.split(',').mapNotNull(String::toIntOrNull)
    }

    override fun solvePart1(input: List<Int>): Int {
        val numberOfPositions = input.count()
        val sortedPositions = input.sorted()
        val medianPositionIndex = numberOfPositions / 2
        val medianPosition = if (numberOfPositions % 2 == 1)  sortedPositions[medianPositionIndex]
            else (sortedPositions[medianPositionIndex] + sortedPositions[medianPositionIndex - 1]) / 2
        return naiveFuelConsumption(input, medianPosition)
    }

    private fun naiveFuelConsumption(positions: List<Int>, targetPosition: Int): Int{
        return positions.sumOf { (targetPosition - it).absoluteValue }
    }

    override fun solvePart2(input: List<Int>): Int {
        val averagePosition = input.average()
        // Let k be the number of positions smaller than x,
        // l the number of occurrences of x as position,
        // m the number of positions larger than x
        // and n = k + l + m the number of positions.
        //The condition that fuel(x+1) >= fuel(x) is equivalent to x >= averagePosition + 2m/n - 1.5 (>= averagePosition - 1.5)
        val lowestPossiblePosition = floor(averagePosition - 1.5).toInt()
        //The condition that fuel(x-1) >= fuel(x) is equivalent to x <= averagePosition - 2k/n + 1.5 (<= averagePosition - 1.5)
        val highestPossiblePosition = ceil(averagePosition + 1.5).toInt()
        return (lowestPossiblePosition..highestPossiblePosition).minOf { realFuelConsumption(input, it) }
    }

    private fun realFuelConsumption(positions: List<Int>, targetPosition: Int): Int{
        return positions.sumOf { (targetPosition - it).absoluteValue * ((targetPosition - it).absoluteValue + 1) / 2 }
    }
}