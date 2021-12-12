package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d
import adventOfCode2021.utility.girdNeighbours


typealias HeightMap = List<List<Int>>

private operator fun HeightMap.get(point: Point2d): Int = this[point.y][point.x]

fun HeightMap.getOrNull(point: Point2d): Int? = this.getOrNull(point.y)?.getOrNull(point.x)

private const val MAX_HEIGHT: Int = 9


class Day9 : Day<HeightMap, Int> {
    override fun parseInput(input: String): HeightMap {
        return input
            .lines()
            .mapNotNull { it.mapNotNull { it.digitToIntOrNull() } }
            .filter { !it.isEmpty() }
    }

    override fun solvePart1(input: HeightMap): Int {
        return riskLevel(input)
    }

    private fun riskLevel(heightMap: HeightMap): Int =
        lowPoints(heightMap).sumOf { riskLevel(it, heightMap) }

    private fun riskLevel(point: Point2d, heightMap: HeightMap): Int = heightMap.getOrNull(point)?.plus(1) ?: 0

    private fun lowPoints(heightMap: HeightMap): Set<Point2d>{
        return heightMap.mapIndexedNotNull { y, row ->
                row.mapIndexedNotNull { x, height ->
                    val point = Point2d(x,y)
                    if (isLowPoint(point, height, heightMap)) point else null
                }
            }.flatten()
            .toSet()
    }

    private fun isLowPoint(point: Point2d, height: Int, heightMap: HeightMap): Boolean{
        return point
            .girdNeighbours()
            .mapNotNull { heightMap.getOrNull(it) }
            .all { it > height }
    }

    override fun solvePart2(input: HeightMap): Int {
        val basins = lowPoints(input).map { basin(it, input) }
        val basinSizes = basins.map(Set<Point2d>::count)
            .sorted()
            .reversed()
        return basinSizes
            .take(3)
            .reduce(Int::times)
    }

    private fun basin(lowPoint: Point2d, heightMap: HeightMap): Set<Point2d>{
        val currentSurfaceLevelLake = mutableSetOf(lowPoint)
        for(nextSurfaceLevel in (heightMap[lowPoint] + 1) until MAX_HEIGHT){
            val nextHeightLevel = borderingHeightLevel(nextSurfaceLevel, currentSurfaceLevelLake, heightMap)
            if (nextHeightLevel.any { isRidgePoint(it, currentSurfaceLevelLake, heightMap) }){
                break
            }
            currentSurfaceLevelLake.addAll(nextHeightLevel)
        }
        return currentSurfaceLevelLake
    }

    private fun borderingHeightLevel(height: Int, lake: Set<Point2d>, heightMap: HeightMap): Set<Point2d>{
        val heightLevel = mutableSetOf<Point2d>()
        var adjacentPoints = fixedHeightNeighbours(height, lake, heightMap)
        while (!adjacentPoints.isEmpty()){
            heightLevel.addAll(adjacentPoints)
            adjacentPoints = fixedHeightNeighbours(height, adjacentPoints, heightMap)
                .filter { !heightLevel.contains(it) }
                .toSet()
        }
        return heightLevel
    }

    private fun fixedHeightNeighbours(height: Int, currentPoints: Set<Point2d>, heightMap: HeightMap): Set<Point2d> =
        currentPoints.map { it.girdNeighbours() }
            .flatten()
            .filter { heightMap.getOrNull(it) == height }
            .toSet()

    //Only works is the lake height is at least one less than the point's.
    private fun isRidgePoint(point: Point2d, lake: Set<Point2d>, heightMap: HeightMap): Boolean{
        val pointHeight = heightMap.getOrNull(point) ?: return false
        return point
            .girdNeighbours()
            .filter { !lake.contains(it) }
            .mapNotNull { heightMap.getOrNull(it) }
            .any { it < pointHeight }
    }
}