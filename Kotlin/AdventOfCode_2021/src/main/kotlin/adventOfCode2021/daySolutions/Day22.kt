package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point3d


data class ReactorCubeSpec(val shouldBeOn: Boolean, val cube: IntCube)
data class IntCube(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)


private const val INITIALIZATION_LOWER_BOUND = -50
private const val INITIALIZATION_UPPER_BOUND = 50

class Day22: Day<List<ReactorCubeSpec>, ULong> {
    override fun parseInput(input: String): List<ReactorCubeSpec> {
        return input.lines().mapNotNull(::parseSpec)
    }

    private fun parseSpec(text: String): ReactorCubeSpec?{
        if(text.length < 22) {
            return null
        }
        val shouldBeOn = text.startsWith("on ")
        if (!shouldBeOn && !text.startsWith("off ")){
            return null
        }
        val coordinateRanges = text
            .drop(if(shouldBeOn) "on ".length else "off ".length)
            .split(',')
            .map { rangeText ->
                rangeText.drop(2)
                    .split("..")
                    .mapNotNull { it.toIntOrNull() }
            }.mapNotNull { if(it.count() == 2) it[0]..it[1] else null }
        if(coordinateRanges.count() != 3){
            return null
        }
        val cube = IntCube(coordinateRanges[0], coordinateRanges[1], coordinateRanges[2])
        return ReactorCubeSpec(shouldBeOn, cube)
    }

    override fun solvePart1(input: List<ReactorCubeSpec>): ULong {
        val activeReactorSpots = initializeReactor(input, INITIALIZATION_LOWER_BOUND..INITIALIZATION_UPPER_BOUND)
        return activeReactorSpots.count().toULong()
    }

    private fun initializeReactor(reactorSpecs: List<ReactorCubeSpec>, initializationBounds: IntRange): Set<Point3d>{
        val activeSpots = mutableSetOf<Point3d>()
        for (spec in reactorSpecs){
            with(spec.cube) {
                for(x in xRange.intersect(initializationBounds)){
                    for(y in yRange.intersect(initializationBounds)){
                        for(z in zRange.intersect(initializationBounds)){
                            if(spec.shouldBeOn){
                                activeSpots.add(Point3d(x, y, z))
                            } else{
                                activeSpots.remove(Point3d(x, y, z))
                            }
                        }
                    }
                }
            }
        }

        return activeSpots
    }

    override fun solvePart2(input: List<ReactorCubeSpec>): ULong {
        val reactorState = reboot(input)
        return reactorState.sumOf { it.count() }
    }

    private fun reboot(reactorSpecs: List<ReactorCubeSpec>): Set<IntCube>{
        return reactorSpecs.fold(setOf()) { reactorState, spec -> applySpec(spec, reactorState) }
    }

    private fun applySpec(spec: ReactorCubeSpec, reactorState: Set<IntCube>): Set<IntCube>{
        var newReactorState = reactorState
            .flatMap { it.remove(spec.cube) }
            .toMutableSet()
        if (spec.shouldBeOn){
            newReactorState.add(spec.cube)
        }
        return newReactorState
    }
}

private fun IntRange.intersect(other: IntRange): IntRange{
    val startIndex = if(other.first > first) other.first else first
    val endIndex = if(other.last < last) other.last else last
    return startIndex..endIndex
}

private fun IntRange.intersectOrNull(other: IntRange): IntRange?{
    val intersection = intersect(other)
    return if (intersection.isEmpty()) null else intersection
}

private fun IntCube.intersect(other: IntCube): IntCube?{
    val xIntersection = xRange.intersectOrNull(other.xRange) ?: return null
    val yIntersection = yRange.intersectOrNull(other.yRange) ?: return null
    val zIntersection = zRange.intersectOrNull(other.zRange) ?: return null
    return IntCube(xIntersection, yIntersection, zIntersection)
}

private fun IntCube.remove(other: IntCube): Set<IntCube>{
    val intersection = intersect(other) ?: return setOf(this)
    if (intersection == this){
        return emptySet()
    }
    val xPartitions = xRange.partitions(intersection.xRange)
    val yPartitions = yRange.partitions(intersection.yRange)
    val zPartitions = zRange.partitions(intersection.zRange)

    val subCubes = xPartitions.flatMap { xPartition ->
        yPartitions.flatMap { yPartition ->
            zPartitions.map { zPartition ->
                IntCube(xPartition, yPartition, zPartition)
            }
        }
    }
    return subCubes
        .filter { it != intersection }
        .toSet()
}

private fun IntRange.partitions(toSeparate: IntRange): List<IntRange>{
    return if (last < toSeparate.first //to the left
        || toSeparate.last < first //to the right
        || toSeparate.first <= first && last <= toSeparate.last){ //inside
        listOf(this)
    } else if (first < toSeparate.first && last <= toSeparate.last){ //overlapping left side
        listOf(first until toSeparate.first, toSeparate.first..last)
    } else if (first >= toSeparate.first && last > toSeparate.last){ //overlapping right side
        listOf(first..toSeparate.last, (toSeparate.last + 1)..last)
    } else { //surrounding
        listOf(first until toSeparate.first, toSeparate, (toSeparate.last + 1)..last)
    }
}

private fun IntRange.length() = if (isEmpty()) 0UL else (last - first + 1).toULong()

private fun IntCube.count(): ULong = xRange.length() * yRange.length() * zRange.length()