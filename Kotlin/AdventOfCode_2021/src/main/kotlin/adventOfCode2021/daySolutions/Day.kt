package adventOfCode2021.daySolutions

interface DaySolver{
    fun solutionForPart1(input: String): String
    fun solutionForPart2(input: String): String
}

interface Day<T, out R>: DaySolver {
    fun parseInput(input: String): T
    fun solvePart1(input: T): R
    fun solvePart2(input: T): R
    override fun solutionForPart1 (input: String): String = solvePart1(parseInput(input)).toString()
    override fun solutionForPart2 (input: String): String = solvePart2(parseInput(input)).toString()
}