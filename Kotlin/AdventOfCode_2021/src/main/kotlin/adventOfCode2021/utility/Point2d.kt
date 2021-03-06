package adventOfCode2021.utility

data class Point2d(val x: Int, val y: Int){
    operator fun plus(other: Point2d): Point2d = Point2d(x + other.x, y + other.y)
    operator fun minus(other: Point2d): Point2d = Point2d(x - other.x, y - other.y)
}

operator fun Int.times(point: Point2d): Point2d = Point2d(this * point.x, this * point.y)

fun Direction2d.toPoint(): Point2d{
    return when(this){
        Direction2d.UP -> Point2d(0, 1)
        Direction2d.LEFT -> Point2d(-1, 0)
        Direction2d.DOWN -> Point2d(0, -1)
        Direction2d.RIGHT -> Point2d(1, 0)
    }
}

private val gridNeighbourOffsets = setOf(
    Point2d(-1, 0),
    Point2d(1, 0),
    Point2d(0, -1),
    Point2d(0, 1)
)

fun Point2d.girdNeighbours(): Set<Point2d>{
    return gridNeighbourOffsets.map { it + this }.toSet()
}

private val fullGridNeighbourOffsets = setOf(
    Point2d(1, 0),
    Point2d(1, 1),
    Point2d(0, 1),
    Point2d(-1, 1),
    Point2d(-1, 0),
    Point2d(-1, -1),
    Point2d(0, -1),
    Point2d(1, -1),
)

fun Point2d.fullGirdNeighbours(): Set<Point2d>{
    return fullGridNeighbourOffsets.map { it + this }.toSet()
}

private val surroundingGridSquareOffsets = listOf(
    Point2d(-1, -1),
    Point2d(0, -1),
    Point2d(1, -1),
    Point2d(-1, 0),
    Point2d(0, 0),
    Point2d(1, 0),
    Point2d(-1, 1),
    Point2d(0, 1),
    Point2d(1, 1),
)

fun Point2d.surroundingGridSquare(): List<Point2d>{
    return surroundingGridSquareOffsets.map { it + this }
}
