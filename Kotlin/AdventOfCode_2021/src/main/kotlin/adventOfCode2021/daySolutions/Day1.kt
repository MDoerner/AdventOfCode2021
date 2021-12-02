package adventOfCode2021.daySolutions

import adventOfCode2021.utility.tryToInt

class Day1: Day<List<Int>, Int> {
    override fun parseInput(input: String): List<Int> {
        return input.lines().mapNotNull(String::tryToInt)
    }

    override fun solvePart1(input: List<Int>): Int = increaseCount(input)

    private fun<T:Comparable<T>> increaseCount(values: Iterable<T>): Int = values
        .windowed(2)
        .fold(0) {currentIncreaseCount, window ->
            if(window[0] < window[1]) currentIncreaseCount + 1 else currentIncreaseCount}


    override fun solvePart2(input: List<Int>): Int = increaseCount(windowAverages(input, 3))

    private fun windowAverages(values: List<Int>, windowSize: Int): List<Double>{
        return values.windowed(windowSize) { it.average() }
    }
}