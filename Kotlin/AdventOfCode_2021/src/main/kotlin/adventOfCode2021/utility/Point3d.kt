package adventOfCode2021.utility

import kotlin.math.abs

data class Point3d(val x: Int, val y: Int, val z: Int){
    operator fun plus(other: Point3d): Point3d = Point3d(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3d): Point3d = Point3d(x - other.x, y - other.y, z - other.z)
    operator fun unaryMinus(): Point3d = Point3d(-x, -y, -z)

    fun rotate90x(steps: Int = 1): Point3d = when(steps % 4){
        0 -> this
        1, -3 -> Point3d(x, z, -y)
        2, -2 -> Point3d(x, -y, -z)
        3, -1 -> Point3d(x, -z, y)
        else -> throw NotImplementedError("This cannot happen because of how modulo works.")
    }

    fun rotate90y(steps: Int = 1): Point3d = when(steps % 4){
        0 -> this
        1, -3 -> Point3d(-z, y, x)
        2, -2 -> Point3d(-x, y, -z)
        3, -1 -> Point3d(z, y, -x)
        else -> throw NotImplementedError("This cannot happen because of how modulo works.")
    }

    fun rotate90z(steps: Int = 1): Point3d = when(steps % 4){
        0 -> this
        1, -3 -> Point3d(-y, x, z)
        2, -2 -> Point3d(-x, -y, z)
        3, -1 -> Point3d(y, -x, z)
        else -> throw NotImplementedError("This cannot happen because of how modulo works.")
    }

    fun l1Norm(): Int = abs(x) + abs(y) + abs(z)
}

fun l1Distance(point1: Point3d, point2: Point3d): Int = (point2 - point1).l1Norm()

operator fun Int.times(point: Point3d): Point3d = Point3d(this * point.x, this * point.y, this * point.z)


private val gridNeighbourOffsets = setOf(
    Point3d(-1, 0, 0),
    Point3d(1, 0, 0),
    Point3d(0, -1, 0),
    Point3d(0, 1, 0),
    Point3d(0, 0, 1),
    Point3d(0, 0, -1),
)

fun Point3d.girdNeighbours(): Set<Point3d>{
    return gridNeighbourOffsets.map { it + this }.toSet()
}

private val fullGridNeighbourOffsets = setOf(
    Point3d(1, 0, -1),
    Point3d(1, 1, -1),
    Point3d(0, 1, -1),
    Point3d(-1, 1, -1),
    Point3d(-1, 0, -1),
    Point3d(-1, -1, -1),
    Point3d(0, -1, -1),
    Point3d(1, -1, -1),
    Point3d(1, 0, 0),
    Point3d(1, 1, 0),
    Point3d(0, 1, 0),
    Point3d(-1, 1, 0),
    Point3d(-1, 0, 0),
    Point3d(-1, -1, 0),
    Point3d(0, -1, 0),
    Point3d(1, -1, 0),
    Point3d(1, 0, 1),
    Point3d(1, 1, 1),
    Point3d(0, 1, 1),
    Point3d(-1, 1, 1),
    Point3d(-1, 0, 1),
    Point3d(-1, -1, 1),
    Point3d(0, -1, 1),
    Point3d(1, -1, 1),
)

fun Point3d.fullGirdNeighbours(): Set<Point3d>{
    return fullGridNeighbourOffsets.map { it + this }.toSet()
}

enum class RotationalCubeSymmetry{
    Id,
    XAxisOnce,
    XAxisTwice,
    XAxisThrice,
    YAxisOnce,
    YAxisTwice,
    YAxisThrice,
    ZAxisOnce,
    ZAxisTwice,
    ZAxisThrice,
    PosXPosYEdge,
    PosXNegYEdge,
    PosXPosZEdge,
    PosXNegZEdge,
    PosYPosZEdge,
    PosYNegZEdge,
    PosXPosYDiagonalOnce,
    PosXPosYDiagonalTwice,
    PosXNegYDiagonalOnce,
    PosXNegYDiagonalTwice,
    NegXNegYDiagonalOnce,
    NegXNegYDiagonalTwice,
    NegXPosYDiagonalOnce,
    NegXPosYDiagonalTwice,
}

fun Point3d.rotate(symmetry: RotationalCubeSymmetry): Point3d = when(symmetry){
    RotationalCubeSymmetry.Id -> this
    RotationalCubeSymmetry.XAxisOnce -> this.rotate90x(1)
    RotationalCubeSymmetry.XAxisTwice -> this.rotate90x(2)
    RotationalCubeSymmetry.XAxisThrice -> this.rotate90x(3)
    RotationalCubeSymmetry.YAxisOnce -> this.rotate90y(1)
    RotationalCubeSymmetry.YAxisTwice -> this.rotate90y(2)
    RotationalCubeSymmetry.YAxisThrice -> this.rotate90y(3)
    RotationalCubeSymmetry.ZAxisOnce -> this.rotate90z(1)
    RotationalCubeSymmetry.ZAxisTwice -> this.rotate90z(2)
    RotationalCubeSymmetry.ZAxisThrice -> this.rotate90z(3)
    RotationalCubeSymmetry.PosXPosYEdge -> Point3d(y, x, -z)
    RotationalCubeSymmetry.PosXNegYEdge -> Point3d(-y, -x, -z)
    RotationalCubeSymmetry.PosXPosZEdge -> Point3d(z, -y, x)
    RotationalCubeSymmetry.PosXNegZEdge -> Point3d(-z, -y, -x)
    RotationalCubeSymmetry.PosYPosZEdge -> Point3d(-x, z, y)
    RotationalCubeSymmetry.PosYNegZEdge -> Point3d(-x, -z, -y)
    RotationalCubeSymmetry.PosXPosYDiagonalOnce -> Point3d(z, x, y)
    RotationalCubeSymmetry.PosXPosYDiagonalTwice -> Point3d(y, z, x)
    RotationalCubeSymmetry.PosXNegYDiagonalOnce -> Point3d(-y, -z, x)
    RotationalCubeSymmetry.PosXNegYDiagonalTwice -> Point3d(z, -x, -y)
    RotationalCubeSymmetry.NegXNegYDiagonalOnce -> Point3d(-z, x, -y)
    RotationalCubeSymmetry.NegXNegYDiagonalTwice -> Point3d(y, -z, -x)
    RotationalCubeSymmetry.NegXPosYDiagonalOnce -> Point3d(-y, z, -x)
    RotationalCubeSymmetry.NegXPosYDiagonalTwice -> Point3d(-z, -x, y)
}