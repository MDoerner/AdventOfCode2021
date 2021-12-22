package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d
import adventOfCode2021.utility.surroundingGridSquare

data class ScannerDate(val algorithm: List<Int>, val image: Map<Point2d, Int>, val background: Int)

class Day20: Day<ScannerDate, Int> {
    override fun parseInput(input: String): ScannerDate {
        val partsRegEx = Regex("""\r?\n\r?\n""")
        val parts = input.split(partsRegEx)
        val algorithm = parseAlgorithm(parts[0])
        val image = parseImage(parts[1])
        val background = 0
        return ScannerDate(algorithm, image, background)
    }

    private fun parseAlgorithm(text:String): List<Int> = text.mapNotNull(::encodeInputChar)

    private fun encodeInputChar(c: Char): Int? =
        when(c) {
            '.' -> 0
            '#' -> 1
            else -> null
        }

    private fun parseImage(text: String): Map<Point2d, Int>{
        return text.lines().flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                val value = encodeInputChar(c) ?: return@mapIndexedNotNull null
                Point2d(x,y) to value
            }
        }.toMap()
    }

    override fun solvePart1(input: ScannerDate): Int {
        val image = input.image.toMutableMap()
        executeSteps(image, input.algorithm, input.background, 2)
        return image
            .filterValues { it == 1 }
            .count()
    }

    private fun executeSteps(image: MutableMap<Point2d, Int>, algorithm: List<Int>, background: Int, numberOfSteps: Int): Int{
        return (1..numberOfSteps)
            .fold(image.keys.toSet() to background) { (changedPoints,currentBackground) , _ -> executeStep(image, algorithm, currentBackground, changedPoints) }
            .second
    }

    private fun executeStep(image: MutableMap<Point2d, Int>, algorithm: List<Int>, background: Int, changedPoints: Set<Point2d>): Pair<Set<Point2d>,Int>{
        val newBackground = if (background == 0) algorithm[0] else algorithm[511]

        val changingPointsWithValues = changedPoints
            .flatMap { it.surroundingGridSquare() }
            .filter { image.containsKey(it) }
            .associateWith { image[it]!! to algorithmOutput(it, image, algorithm, background) }
            .filterValues { (old, new) -> old != new }
            .mapValues { it.value.second }

        val newPoints = image.keys
            .flatMap { it.surroundingGridSquare() }
            .filter { !image.containsKey(it) }
        val newPointsWithValues = newPoints.associateWith { algorithmOutput(it, image, algorithm, background) }
        val newPointsMergeWithBackground = newPointsWithValues.all { it.value == newBackground }

        if (!newPointsMergeWithBackground){
            image.putAll(newPointsWithValues)
        }
        image.putAll(changingPointsWithValues)

        val changingPoints = if (newPointsMergeWithBackground) {
            changingPointsWithValues.keys
        } else {
            changingPointsWithValues.keys + newPoints
        }

        return changingPoints to newBackground
    }

    private fun algorithmOutput(point: Point2d, image: MutableMap<Point2d, Int>, algorithm: List<Int>, background: Int): Int{
        val algorithmInput = point
            .surroundingGridSquare()
            .map { image.getOrDefault(it, background) }
            .fold(0) { acc, digit -> (acc shl 1) + digit }
        return algorithm[algorithmInput]
    }


    override fun solvePart2(input: ScannerDate): Int {
        val image = input.image.toMutableMap()
        executeSteps(image, input.algorithm, input.background, 50)
        return image
            .filterValues { it == 1 }
            .count()
    }
}