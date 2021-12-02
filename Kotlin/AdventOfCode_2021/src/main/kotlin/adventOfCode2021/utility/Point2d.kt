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