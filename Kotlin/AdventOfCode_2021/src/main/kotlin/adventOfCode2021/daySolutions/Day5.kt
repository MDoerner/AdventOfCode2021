package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d


data class Line2d(val startPoint: Point2d, val endPoint: Point2d){
    val isHorizontal: Boolean
        get() = startPoint.y == endPoint.y

    val isVertical: Boolean
        get() = startPoint.x == endPoint.x

    private val isAscendingDiagonal: Boolean
        get() = (startPoint.x - endPoint.x) == (startPoint.y - endPoint.y)

    private val isDescendingDiagonal: Boolean
        get() = (startPoint.x - endPoint.x) == -(startPoint.y - endPoint.y)

    val isDiagonal: Boolean
        get() = isAscendingDiagonal || isDescendingDiagonal

    fun containedPoints(): List<Point2d>{
        return if (isVertical) intProgressionFromEndPoints(startPoint.y, endPoint.y).map { Point2d(startPoint.x, it) }
            else if (isHorizontal) intProgressionFromEndPoints(startPoint.x, endPoint.x).map { Point2d(it, startPoint.y) }
            else if (isAscendingDiagonal) intProgressionFromEndPoints(0, endPoint.x - startPoint.x).map { startPoint + Point2d(it, it) }
            else if (isDescendingDiagonal) intProgressionFromEndPoints(0, endPoint.x - startPoint.x).map { startPoint + Point2d(it, -it) }
            else listOf(startPoint, endPoint)
    }
}

fun intProgressionFromEndPoints(start: Int, end: Int): IntProgression =
    IntProgression.fromClosedRange(start, end, if (start <= end) 1 else -1)

class Day5 : Day<List<Line2d>, Int> {
    private val lineRegEx = Regex("""^(\d+),(\d+) -> (\d+),(\d+)$""")

    override fun parseInput(input: String): List<Line2d>{
        return input.lines().mapNotNull(::parseLine)
    }

    private fun parseLine(text: String): Line2d?{
        val matchedGroups = lineRegEx.matchEntire(text)?.groupValues ?: return null
        val startX = matchedGroups[1].toIntOrNull() ?: return null
        val startY = matchedGroups[2].toIntOrNull() ?: return null
        val endX = matchedGroups[3].toIntOrNull() ?: return null
        val endY = matchedGroups[4].toIntOrNull() ?: return null
        return Line2d(Point2d(startX, startY), Point2d(endX, endY))
    }

    override fun solvePart1(input: List<Line2d>): Int {
        val gridAlignedLines = input.filter { it.isVertical || it.isHorizontal }
        val pointsWithAtLeastTwoGridAlignedLines = linesOnPoint(gridAlignedLines).filter { it.value.count() >= 2 }
        return pointsWithAtLeastTwoGridAlignedLines.count()
    }

    private fun linesOnPoint(lines: List<Line2d>): Map<Point2d, List<Line2d>> = lines
        .map { line -> line.containedPoints()
                .map {point -> point to line}
        }.flatten()
        .groupBy({(point, _) -> point}, {(_ , line) -> line})

    override fun solvePart2(input: List<Line2d>): Int {
        val gridAlignedLines = input.filter { it.isVertical || it.isHorizontal || it.isDiagonal }
        val pointsWithAtLeastTwoGridAlignedLines = linesOnPoint(gridAlignedLines).filter { it.value.count() >= 2 }
        return pointsWithAtLeastTwoGridAlignedLines.count()
    }
}