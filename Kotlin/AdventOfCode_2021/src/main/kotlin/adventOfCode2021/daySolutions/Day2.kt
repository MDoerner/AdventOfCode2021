package adventOfCode2021.daySolutions

import adventOfCode2021.utility.*

data class MovementInstruction(val direction: Direction2d, val magnitude: Int){
    fun toPoint(): Point2d = magnitude * direction.toPoint()
}


class Day2: Day<List<MovementInstruction>, Int> {
    private val instructionRegex: Regex = Regex("^(\\w+)(\\s+)(\\d+)$")

    override fun parseInput(input: String): List<MovementInstruction> {
        return input.lines().mapNotNull(::parseInstruction)
    }

    private fun parseInstruction(instruction: String): MovementInstruction?{
        val matchedGroups = instructionRegex.matchEntire(instruction)?.groupValues ?: return null
        val magnitude = matchedGroups[3].tryToInt() ?: return null
        val direction = when(matchedGroups[1]){
            "forward" -> Direction2d.RIGHT
            "up" -> Direction2d.UP
            "down" -> Direction2d.DOWN
            else -> null
         } ?: return null
        return MovementInstruction(direction, magnitude)
    }

    override fun solvePart1(input: List<MovementInstruction>): Int {
        val destination = input.fold(Point2d(0,0)) { location, instruction -> location + instruction.toPoint() }
        return -destination.y * destination.x
    }

    override fun solvePart2(input: List<MovementInstruction>): Int {
        val (_finalAim, destination) = input.fold(0 to Point2d(0,0)) {
            (aim, location), instruction -> when(instruction.direction){
                Direction2d.UP -> (aim + instruction.magnitude) to location
                Direction2d.DOWN -> (aim - instruction.magnitude) to location
                Direction2d.RIGHT -> aim to (location + instruction.magnitude * Point2d(1, aim))
                Direction2d.LEFT -> aim to (location - instruction.magnitude * Point2d(1, aim))
            }
        }
        return -destination.y * destination.x
    }

}