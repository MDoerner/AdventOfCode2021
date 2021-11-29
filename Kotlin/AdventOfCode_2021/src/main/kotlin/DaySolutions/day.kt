package daySolutions

interface DaySolver{
    fun solutionForPart1(input: String): String
    fun solutionForPart2(input: String): String
}

interface Day<T>:DaySolver{
    fun parseInput(input: String): T
    fun solvePart1(input: T): String
    fun solvePart2(input: T): String
    override fun solutionForPart1 (input: String): String = solvePart1(parseInput(input))
    override fun solutionForPart2 (input: String): String = solvePart2(parseInput(input))
}