package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d
import adventOfCode2021.utility.fullGirdNeighbours


typealias JellyMap = MutableList<MutableList<Int>>

private operator fun JellyMap.get(point: Point2d): Int = this[point.y][point.x]
private fun JellyMap.getOrNull(point: Point2d): Int? = this.getOrNull(point.y)?.getOrNull(point.x)
private operator fun JellyMap.set(point: Point2d, value: Int){
    this[point.y][point.x] = value
}

private const val FLASHING_LEVEL = 10

private fun JellyMap.incrementAll(){
    for ( jellyRow in this ){
        for (index in 0 until jellyRow.count()){
            jellyRow[index]++
        }
    }
}

private fun JellyMap.flash(){
    val initiallyReadyJellyCoordinates = this.mapIndexed { y , jellyRow ->
        jellyRow.mapIndexedNotNull { x , jellyLevel ->
            if (jellyLevel == FLASHING_LEVEL ) x to y else null
        }
    }.flatten()
    for ((x, y) in initiallyReadyJellyCoordinates){
        this.flashIfReady(Point2d(x, y))
    }
    this.resetFlashed()
}

private fun JellyMap.flashIfReady(flashingJelly: Point2d){
    if (this[flashingJelly] == FLASHING_LEVEL) {
        this.flash(flashingJelly)
    }
}

private fun JellyMap.flash(flashingJelly: Point2d){
    this[flashingJelly]++ //Ensures that the jelly is recognized as already flashed.
    for (neighbour in flashingJelly.fullGirdNeighbours()){
        val neighbourLevel = this.getOrNull(neighbour) ?: continue
        when(neighbourLevel){
            FLASHING_LEVEL -> this.flash(neighbour)
            FLASHING_LEVEL - 1 -> {
                this[neighbour] = FLASHING_LEVEL
                this.flash(neighbour)
            }
            else -> this[neighbour]++
        }
    }
}

private fun JellyMap.resetFlashed(){
    for ( jellyRow in this ){
        for (index in 0 until jellyRow.count()){
            if ( jellyRow[index] > FLASHING_LEVEL){
                jellyRow[index] = 0
            }
        }
    }
}

class Day11 : Day<JellyMap, Int> {
    override fun parseInput(input: String): JellyMap {
        return input
                .lines()
            .map { it.mapNotNull({ it.digitToIntOrNull() }).toMutableList()}
            .filter(MutableList<Int>::isNotEmpty)
            .toMutableList()
    }

    override fun solvePart1(input: JellyMap): Int {
        return executeStepsAndCountFlashes(input, 100)
    }

    private fun executeStepsAndCountFlashes(jellies: JellyMap, numberOfSteps: Int): Int{
        return (1..numberOfSteps).sumOf { executeStepAndCountFlashes(jellies) }
    }

    private fun executeStepAndCountFlashes(jellies: JellyMap): Int{
        jellies.incrementAll()
        jellies.flash()
        return jellies.sumOf { jellyRow -> jellyRow.count { it == 0 } }
    }

    override fun solvePart2(input: JellyMap): Int {
        var stepNumber = 1
        while (executeStepAndCountFlashes(input) != 100){
            stepNumber++
        }
        return stepNumber
    }
}