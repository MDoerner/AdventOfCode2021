package adventOfCode2021.daySolutions

import kotlin.math.abs


typealias RoomOccupancy = List<List<Char?>>

private const val LARGE_VALUE = 1000000

class Day23: Day<Pair<RoomOccupancy, Int>, Int> {
    override fun parseInput(input: String): Pair<RoomOccupancy, Int> {
        val lines = input.lines()
        val rooms = listOf(
            listOf(lines[2][3], lines[3][3]),
            listOf(lines[2][5], lines[3][5]),
            listOf(lines[2][7], lines[3][7]),
            listOf(lines[2][9], lines[3][9])
        )
        val initialUpperBound = LARGE_VALUE
        return rooms to initialUpperBound
    }

    override fun solvePart1(input: Pair<RoomOccupancy, Int>): Int {
        return minCost(input.first, input.second)
    }

    private fun minCost(initialRooms: RoomOccupancy, costUpperBound: Int): Int{
        val costLowerBound = lowerCostBound(initialRooms)
        val additionalCostUpperBound = costUpperBound - costLowerBound
        val initialOccupants = initialRooms.flatMapIndexed { roomIndex, room ->
            room.mapIndexed { depth, type ->
                val isDone = targetRoom[type] == roomIndex && room.drop(depth + 1).all { it == type }
                OccupantState(type!!, roomPosition(roomIndex), depth + 1, isDone)
            }
        }
        val (minAdditional, example) = minAdditionalCost(initialOccupants, initialRooms, additionalCostUpperBound) //example for debugging
        return costLowerBound + minAdditional
    }

    private fun minAdditionalCost(occupantStates: List<OccupantState>, rooms: RoomOccupancy, costUpperBound: Int): Pair<Int, List<Triple<OccupantState, Int, Int>>>{
        if (occupantStates.all { it.isDone }){
            return 0 to emptyList()
        }
        var currentBest = costUpperBound
        var currentBestMoves = emptyList<Triple<OccupantState, Int, Int>>()
        var isDeadEnd = true
        val occupiedHallwayPositions = occupantStates
            .filter { it.roomDepth == 0 }
            .map { it.position }
        for ((index, occupant) in occupantStates.withIndex()){
            val newStates = occupantStates.toMutableList()
            for(newPosition in occupant.possibleMoves(occupiedHallwayPositions, rooms)){
                val moveCost = occupant.additionalMoveCost(newPosition)
                val remainingAdditionalCostAllowance = currentBest - moveCost
                    if (remainingAdditionalCostAllowance < 0){
                    continue
                }
                val (newState, newRooms) = occupant.move(newPosition, rooms)
                newStates[index] = newState
                val (bestAdditionalCost, bestFurtherMoves) = minAdditionalCost(newStates, newRooms, remainingAdditionalCostAllowance)
                if (bestAdditionalCost <= remainingAdditionalCostAllowance){
                    currentBest = bestAdditionalCost + moveCost
                    currentBestMoves = listOf(Triple(occupant, newPosition, moveCost)) + bestFurtherMoves
                    isDeadEnd = false
                }
            }
        }
        return if(isDeadEnd) LARGE_VALUE to emptyList() else currentBest to currentBestMoves
    }

    private fun lowerCostBound(initialState: RoomOccupancy): Int{
        val hallwayCost = initialState.mapIndexed { roomIndex, room ->
            room.sumOf { occupant ->
                val result = 2 * movementCost.getOrDefault(occupant, 0) * abs(roomIndex - targetRoom.getOrDefault(occupant, -1))
                result
            }
        }.sum()
        val (leaveRoomCost, enterRoomCost) = initialState.mapIndexed { roomIndex, room ->
            val roomOwner = targetRoom
                .filterValues { it == roomIndex }
                .keys
                .single()
            val alreadyInPlaceCount = room
                .takeLastWhile { it == null || it == roomOwner}
                .count()

            val enterSteps = (room.count() - alreadyInPlaceCount) * (room.count() - alreadyInPlaceCount + 1) / 2
            val enterCost = enterSteps * movementCost.getOrDefault(roomOwner, LARGE_VALUE)

            val notInPlace = room.dropLast(alreadyInPlaceCount)
            val leaveCost =  notInPlace.mapIndexed { depth, type ->
                (depth + 1) * movementCost.getOrDefault(type, LARGE_VALUE)
            }.sum()

            leaveCost to enterCost
        }.fold(0 to 0) { (sum1, sum2), (item1, item2) -> (sum1 + item1) to (sum2 + item2) }
        return leaveRoomCost + enterRoomCost + hallwayCost
    }

    override fun solvePart2(input: Pair<RoomOccupancy, Int>): Int {
        val (standardRooms, initialUpperBound) = input
        val rooms = listOf(
            listOf(standardRooms[0][0],'D','D',standardRooms[0][1]),
            listOf(standardRooms[1][0],'C','B',standardRooms[1][1]),
            listOf(standardRooms[2][0],'B','A',standardRooms[2][1]),
            listOf(standardRooms[3][0],'A','C',standardRooms[3][1]),
        )
        return minCost(rooms, initialUpperBound)
    }
}

private val movementCost = mapOf(
    'A' to 1,
    'B' to 10,
    'C' to 100,
    'D' to 1000,
    null to 0
)

private val targetRoom = mapOf(
    'A' to 0,
    'B' to 1,
    'C' to 2,
    'D' to 3,
)

private fun roomPosition(roomIndex: Int): Int = 2 + 2 * roomIndex
private fun roomAtPosition(position: Int): Int = (position - 2)/2
private val roomPositions = targetRoom.values.map(::roomPosition)

private data class OccupantState(val type: Char, val position: Int, val roomDepth: Int, val isDone: Boolean){
    fun possibleMoves(occupiedHallwayPositions: List<Int>, rooms: RoomOccupancy): List<Int>{
        if(isDone || roomDepth > 1 && rooms[roomAtPosition(position)][roomDepth - 2] != null){
            return emptyList()
        }
        if (roomDepth == 0){
            val target = targetRoom[type] ?: return emptyList()
            val targetRoom = rooms[target]
            val targetPosition = roomPosition(target)
            val roomCanBeEntered = targetRoom.all { it == null || it == type }
            val obstacleInTheWay = occupiedHallwayPositions
                .any { it in (targetPosition + 1) until position
                        || it in (position + 1) until targetPosition }
            return if (roomCanBeEntered && !obstacleInTheWay){
                listOf(targetPosition)
            } else {
                emptyList()
            }
        }
        val minPosition = (occupiedHallwayPositions
            .filter { it < position }
            .maxOfOrNull { it } ?: -1) + 1
        val maxPosition = (occupiedHallwayPositions
            .filter { it > position }
            .minOfOrNull { it } ?: 11) - 1
        return (minPosition..maxPosition).filter { it !in roomPositions }
    }

    fun move(newPosition: Int, rooms: RoomOccupancy): Pair<OccupantState, RoomOccupancy>{
        val targetRoomIndex = targetRoom[type]!!
        val targetRoom = rooms[targetRoomIndex]
        val (newRoomDepth, newTargetRoom) = if(newPosition == roomPosition(targetRoomIndex)){
            val openSpots = targetRoom.count { it == null }
            openSpots to (List(openSpots - 1) { null } + List(targetRoom.count() - openSpots + 1) { type })
        } else {
            0 to targetRoom
        }
        val newRooms = rooms.toMutableList()
        newRooms[targetRoomIndex] = newTargetRoom
        if(roomDepth != 0){
            val currentRoomIndex = roomAtPosition(position)
            val newCurrentRoom = List(roomDepth) { null } + rooms[currentRoomIndex].drop(roomDepth)
            newRooms[currentRoomIndex] = newCurrentRoom
        }
        val newIsDone = newRoomDepth != 0
        val newState = OccupantState(type, newPosition, newRoomDepth, newIsDone)
        return newState to newRooms
    }

    fun additionalMoveCost(newPosition: Int): Int {
        val targetPosition = roomPosition(targetRoom.getOrDefault(type,-1))
        val additionalSteps = 2 * if (targetPosition in position..newPosition){
            newPosition - targetPosition
        } else if (targetPosition in newPosition..position){
            targetPosition - newPosition
        } else if (position in targetPosition..newPosition){
            newPosition - position
        } else if (position in newPosition..targetPosition){
            position - newPosition
        } else {
            0
        }
        return additionalSteps * moveCost()
    }

    fun moveCost(): Int = movementCost.getOrDefault(type, LARGE_VALUE)
}