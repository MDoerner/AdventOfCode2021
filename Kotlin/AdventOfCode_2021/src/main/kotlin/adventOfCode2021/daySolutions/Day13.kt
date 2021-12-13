package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d

enum class FoldAxis{
    X,
    Y,
}

data class FoldingPage(val dots: Set<Point2d>, val folds: List<Crease>)
data class Crease(val orientation: FoldAxis, val level: Int)

private fun Point2d.foldOnCrease(foldLine: Crease): Point2d{
    return when(foldLine.orientation){
        FoldAxis.X -> this.foldLeftOnAxis(foldLine.level)
        FoldAxis.Y -> this.foldUpOnAxis(foldLine.level)
    }
}

private fun Point2d.foldUpOnAxis(foldLine: Int): Point2d{
    if ( this.y <= foldLine) {
        return this
    }
    val newY = foldLine - (this.y - foldLine)
    return Point2d(this.x, newY)
}

private fun Point2d.foldLeftOnAxis(foldLine: Int): Point2d{
    if ( this.x <= foldLine) {
        return this
    }
    val newX = foldLine - (this.x - foldLine)
    return Point2d(newX, this.y)
}


class Day13 : Day<FoldingPage, String> {
    override fun parseInput(input: String): FoldingPage {
        val partSeparationRegEx = Regex("""\r?\n\r?\n""")
        val parts = input.split(partSeparationRegEx)
        val points = parts[0].lines().mapNotNull(::parsePoint).toSet()
        val folds = parts[1].lines().mapNotNull(::parseFoldLine)
        return FoldingPage(points, folds)
    }

    private fun parsePoint(text: String): Point2d?{
        val coordinates = text
            .split(',')
            .mapNotNull(String::toIntOrNull)
        return if (coordinates.count() == 2) Point2d(coordinates[0], coordinates[1]) else null
    }

    private fun parseFoldLine(text: String): Crease?{
        val specParts = text
            .drop("fold along ".length)
            .split('=')
        if (specParts.count() != 2) {
            return null
        }
        val foldOrientation = when(specParts[0]){
            "x" -> FoldAxis.X
            "y" -> FoldAxis.Y
            else -> return null
        }
        val foldLevel = specParts[1].toIntOrNull() ?: return null
        return Crease(foldOrientation, foldLevel)
    }

    override fun solvePart1(input: FoldingPage): String {
        return foldResult(input.dots, input.folds[0]).count().toString()
    }

    private fun foldResult(dots: Set<Point2d>, foldLine: Crease): Set<Point2d>{
        return dots.map { it.foldOnCrease(foldLine) }
            .toSet()
    }

    override fun solvePart2(input: FoldingPage): String {
        val result = foldResult(input.dots, input.folds)
        return drawDots(result)
    }

    fun foldResult(dots: Set<Point2d>, foldLines: List<Crease>): Set<Point2d>{
        return foldLines.fold(dots) { currentDots, foldLine -> foldResult(currentDots, foldLine)}
    }

    private fun drawDots(dots: Set<Point2d>): String{
        val minX = dots.minOf { it.x }
        val maxX = dots.maxOf { it.x }
        val minY = dots.minOf { it.y }
        val maxY = dots.maxOf { it.y }

        val builder = StringBuilder()
        for(y in minY..maxY){
            for(x in minX..maxX){
                val nextChar = if (dots.contains(Point2d(x, y))) '#' else '.'
                builder.append(nextChar)
            }
            builder.append("\n")
        }
        return builder.toString()
    }
}