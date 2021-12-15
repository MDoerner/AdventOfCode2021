package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d
import adventOfCode2021.utility.girdNeighbours
import java.util.*


typealias RiskMap = List<List<Int>>

private operator fun RiskMap.get(point: Point2d): Int = this[point.y][point.x]

private fun RiskMap.getOrNull(point: Point2d): Int? = this.getOrNull(point.y)?.getOrNull(point.x)

private fun RiskMap.unfold(times: UInt): RiskMap{
    val firstRows = this.map { row ->
        val rowLength = row.count()
        (0 until times.toInt() * rowLength).map { columnIndex ->
            (row[columnIndex % rowLength] - 1 + columnIndex / rowLength) % 9 + 1
        }
    }
    val columnLength = firstRows.count()
    return (0 until times.toInt() * columnLength).map { rowIndex ->
        val blockRow = rowIndex / columnLength
        firstRows[ rowIndex % columnLength ].map { (it + blockRow - 1) % 9 + 1  }
    }
}


class Day15 : Day<RiskMap, Int> {
    override fun parseInput(input: String): RiskMap {
        return input
            .lines()
            .mapNotNull { it.mapNotNull { it.digitToIntOrNull() } }
            .filter { !it.isEmpty() }
    }

    override fun solvePart1(input: RiskMap): Int {
        return leastRiskDiagonalPathRisk(input)
    }

    private fun leastRiskDiagonalPathRisk(riskMap: RiskMap): Int{
        val startPoint = Point2d(0, 0)
        val endPoint = Point2d(riskMap[0].count() - 1, riskMap.count() - 1)
        return dijkstra(startPoint, endPoint, riskMap)
    }

    private fun dijkstra(startPoint: Point2d, endPoint: Point2d, riskMap: RiskMap): Int {
        val risk = mutableMapOf( startPoint to 0 )
        val riskComparator = kotlin.Comparator<Point2d> { p1, p2 -> risk.getOrDefault(p1, Int.MAX_VALUE) - risk.getOrDefault(p2, Int.MAX_VALUE) }
        val pointsToConsider = PriorityQueue(riskComparator)
        pointsToConsider.add(startPoint)

        var nextPoint = pointsToConsider.poll()
        while (nextPoint != null && nextPoint != endPoint){
            val locationRisk = risk[nextPoint] ?: Int.MAX_VALUE
            for (neighbour in nextPoint.girdNeighbours()){
                val neighbourRisk = riskMap.getOrNull(neighbour) ?: continue
                val currentRisk = risk[neighbour]
                if (currentRisk == null || currentRisk > locationRisk + neighbourRisk){
                    risk[neighbour] = locationRisk + neighbourRisk
                    pointsToConsider.add(neighbour)
                }
            }
            nextPoint = pointsToConsider.poll()
        }

        return if (nextPoint == null) Int.MAX_VALUE else risk[endPoint] ?: Int.MAX_VALUE
    }

    override fun solvePart2(input: RiskMap): Int {
        val riskMap = input.unfold(5U)
        return leastRiskDiagonalPathRisk(riskMap)
    }
}