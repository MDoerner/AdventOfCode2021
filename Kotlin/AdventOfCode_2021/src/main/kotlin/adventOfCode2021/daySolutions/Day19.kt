package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point3d
import adventOfCode2021.utility.RotationalCubeSymmetry
import adventOfCode2021.utility.l1Distance
import adventOfCode2021.utility.rotate
import java.util.*
import kotlin.math.abs
import kotlin.math.min

data class Scanner(val id: Int, val beacons: List<Point3d>)

private const val NECESSARY_OVERLAP_COUNT = 12

class Day19 : Day<List<Scanner>, Int> {
    override fun parseInput(input: String): List<Scanner> {
        val scannerSeparationRegEx = Regex("""\r?\n\r?\n""")
        val scannerTexts = input.split(scannerSeparationRegEx)
        return scannerTexts.mapNotNull(::parseScanner)
    }

    private fun parseScanner(text: String): Scanner?{
        val lines = text.lines()
        val scannerLine = lines.getOrNull(0) ?: return null
        val scannerId = parseScannerId(scannerLine) ?: return null
        val beacons = lines.drop(1).mapNotNull(::parseBeacon)
        return Scanner(scannerId, beacons)
    }

    private fun parseScannerId(text: String): Int?{
        val startIndex = "--- scanner ".length
        val endIndexed = text.length - " ---".length
        val idText = text.substring(startIndex, endIndexed)
        return idText.toIntOrNull()
    }

    private fun parseBeacon(text: String): Point3d?{
        val coords = text.split(',').map { it.toIntOrNull() ?: return null }
        if(coords.count() != 3){
            return null
        }
        return Point3d(coords[0], coords[1], coords[2])
    }

    override fun solvePart1(input: List<Scanner>): Int {
        val positions = scannerPositions(input)
        val beacons = beaconPositions(input, positions)
        return beacons.count()
    }

    private fun beaconPositions(scanners: List<Scanner>, scannerPositions: Map<Int, Pair<Point3d, RotationalCubeSymmetry>>): Set<Point3d>{
        return scanners.flatMap { scanner ->
            val (offset, rotation) = scannerPositions[scanner.id] ?: return@flatMap listOf<Point3d>()
            scanner.beacons.map { beacon -> beaconPosition(beacon, offset, rotation) }
        }.toSet()
    }

    private fun beaconPosition(beacon: Point3d, offset: Point3d, rotation: RotationalCubeSymmetry): Point3d = offset + beacon.rotate(rotation)

    //Returns a pair consisting of the offset from scanner 0 and the rotations needed to align with scanner 0.
    private fun scannerPositions(scanners: List<Scanner>): Map<Int, Pair<Point3d, RotationalCubeSymmetry>>{
        val pairDistances = scanners.associate { it.id to pairDistances(it.beacons) }
        val knownPositions = mutableMapOf(0 to (Point3d(0,0,0) to RotationalCubeSymmetry.Id))
        val alreadyKnownScanners = mutableSetOf(0)
        val scannersToProcess: Queue<Int> = ArrayDeque()
        scannersToProcess.add(0)
        while (!scannersToProcess.isEmpty()){
            val scannerId = scannersToProcess.remove()
            val (scannerOffset, scannerRotation) = knownPositions[scannerId]!!
            val newNeighbours = newNeighbourScanners(scannerId, scannerOffset, scannerRotation, pairDistances, alreadyKnownScanners)
            for ((id, position) in newNeighbours){
                knownPositions[id] = position
            }
            alreadyKnownScanners.addAll(newNeighbours.keys)
            scannersToProcess.addAll(newNeighbours.keys)
        }
        return knownPositions
    }

    private fun pairDistances(beacons: List<Point3d>): Map<Int, List<Pair<Point3d, Point3d>>>{
        val pairs = beacons.flatMapIndexed { firstIndex, first ->
            beacons.mapIndexedNotNull { secondIndex, second ->
                if(firstIndex < secondIndex) first to second else null
            }
        }
        return pairs
            .groupBy { l1Distance(it.first, it.second) }
            .toMap()
    }

    private fun newNeighbourScanners(
        scannerId: Int,
        scannerOffset: Point3d,
        scannerOrientation: RotationalCubeSymmetry,
        pairDistances: Map<Int, Map<Int, List<Pair<Point3d, Point3d>>>>,
        alreadyKnownScanners: Set<Int>
    ): Map<Int, Pair<Point3d, RotationalCubeSymmetry>>{
        val basePairs = pairDistances[scannerId] ?: return mapOf()
        val orientedBasePoints = basePairs.mapValues {
            it.value.map {
                    (first, second) -> first.rotate(scannerOrientation) to second.rotate(scannerOrientation)
            }
        }
        return pairDistances
            .filterKeys { it !in alreadyKnownScanners }
            .mapValues { scannerPositionOrNull(it.value, orientedBasePoints, scannerOffset) }
            .filterValues { it != null }
            .mapValues { it.value!! }
    }

    private fun scannerPositionOrNull(
        beaconPairs: Map<Int, List<Pair<Point3d, Point3d>>>,
        baseBeaconPairs: Map<Int, List<Pair<Point3d, Point3d>>>,
        baseOffset: Point3d
    ): Pair<Point3d, RotationalCubeSymmetry>?{
        val commonDistances = beaconPairs.keys intersect baseBeaconPairs.keys
        val commonDistancePairs = beaconPairs.filterKeys { it in commonDistances }
        val commonDistanceBasePairs = baseBeaconPairs.filterKeys { it in commonDistances }

        if (!areEnoughPairs(commonDistancePairs, commonDistanceBasePairs)){
            return null
        }

        val points = pointsFromPairs(commonDistancePairs)
        val basePoints = pointsFromPairs(commonDistanceBasePairs)

        var remainingBasePairs = commonDistanceBasePairs.values.flatten().count()
        var remainingPairs = commonDistancePairs.values.flatten().count()
        for (distance in commonDistances.sortedWith(compareBy({ commonDistanceBasePairs[it]!!.count() },{ commonDistancePairs[it]!!.count() }))){
            val basePairs = commonDistanceBasePairs[distance]!!
            val pairs = commonDistancePairs[distance]!!

            val potentialPositionOffset = findPairBasedPosition(pairs, basePairs, points, basePoints)
            if (potentialPositionOffset != null){
                val (offset, rotation) = potentialPositionOffset
                return (offset + baseOffset) to rotation
            }

            remainingBasePairs -= basePairs.count()
            remainingPairs -= pairs.count()
            if (remainingBasePairs < 66 || remainingPairs < 66){
                return null
            }
        }
        return null
    }

    private fun areEnoughPairs(pairsByDistance: Map<Int, List<Pair<Point3d, Point3d>>>, otherPairsByDistance: Map<Int, List<Pair<Point3d, Point3d>>>): Boolean{
        val commonDistanceCountSum = pairsByDistance
            .map { min(it.value.count(), otherPairsByDistance[it.key]!!.count()) }
            .sum()
        return commonDistanceCountSum >= 66
    }

    private fun pointsFromPairs(pairs: Map<Int, List<Pair<Point3d, Point3d>>>): Set<Point3d>{
        return pairs.values.flatMap{it.map(Pair<Point3d, Point3d>::first) }.toSet() + pairs.values.flatMap{it.map(Pair<Point3d, Point3d>::second) }.toSet()
    }

    private fun findPairBasedPosition(
        sourcePairs: List<Pair<Point3d, Point3d>>,
        targetPairs: List<Pair<Point3d, Point3d>>,
        sourcePoints: Set<Point3d>,
        targetPoints: Set<Point3d>
    ): Pair<Point3d, RotationalCubeSymmetry>?{
        val targetOffsetDiffs = targetPairs.map { it.first to it.second - it.first }
        for (pair in sourcePairs){
            val pairOffset = pair.second - pair.first
            val matchingTargets = targetOffsetDiffs.filter { pairOffset.canBeRotatedIntoOrIntoNeg(it.second) }
            for(target in matchingTargets){
                val rotation = pairOffset.rotationIntoOrIntoNeg(target.second)!!
                val rotated = pairOffset.rotate(rotation)
                val offset = if (rotated == target.second){
                    target.first - pair.first.rotate(rotation)
                } else {
                    (target.first + target.second) - pair.first.rotate(rotation)
                }
                if(mapsSourceToTarget(offset, rotation, sourcePoints, targetPoints)){
                    return offset to rotation
                }
            }
        }
        return null
    }

    private fun Point3d.canBeRotatedIntoOrIntoNeg(target: Point3d): Boolean{
        val coordinateAmplitudes = setOf(abs(x), abs(y), abs(z))
        val targetCoordinateAmplitudes = setOf(abs(target.x), abs(target.y), abs(target.z))
        return coordinateAmplitudes == targetCoordinateAmplitudes
    }

    private fun Point3d.rotationIntoOrIntoNeg(target: Point3d): RotationalCubeSymmetry?{
        val negTarget = -target
        for (rotation in RotationalCubeSymmetry.values()){
            val rotated = this.rotate(rotation)
            if (rotated == target || rotated == negTarget){
                return rotation
            }
        }
        return null
    }

    private fun mapsSourceToTarget(offset: Point3d, rotation: RotationalCubeSymmetry, sourcePoints: Set<Point3d>, targetPoints: Set<Point3d>): Boolean{
        return sourcePoints.count { beaconPosition(it, offset, rotation) in targetPoints } >= NECESSARY_OVERLAP_COUNT
    }


    override fun solvePart2(input: List<Scanner>): Int {
        val positions = scannerPositions(input)
        return positions.maxOf { first -> positions.maxOf { second -> l1Distance(first.value.first, second.value.first) } }
    }
}