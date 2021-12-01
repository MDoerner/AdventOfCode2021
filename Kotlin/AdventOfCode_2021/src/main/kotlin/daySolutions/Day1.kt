package daySolutions

class Day1:Day<List<Int>,Int> {
    override fun parseInput(input: String): List<Int> {
        return input
            .lines()
            .map { try { it.toInt() } catch (e : NumberFormatException) { null } }
            .filterNotNull()
    }

    override fun solvePart1(input: List<Int>): Int = IncreaseCount(input)

    private fun<T:Comparable<T>> IncreaseCount(values: Iterable<T>): Int = values
        .windowed(2)
        .fold(0) {currentIncreaseCount, window ->
            if(window[0] < window[1]) currentIncreaseCount + 1 else currentIncreaseCount}


    override fun solvePart2(input: List<Int>): Int = IncreaseCount(WindowAverages(input, 3))

    private fun WindowAverages(values: List<Int>, windowSize: Int): List<Double>{
        return values.windowed(windowSize).map { it.average() }
    }
}