package adventOfCode2021.daySolutions

import kotlin.math.*


class Day17 : Day<Pair<IntRange, IntRange>, Int> {
    override fun parseInput(input: String): Pair<IntRange, IntRange> {
        val coordinateBounds = input
            .drop("target area: x=".length)
            .split(", y=")
            .map { it.split("..").map(String::toInt) }
        return coordinateBounds[0][0]..coordinateBounds[0][1] to coordinateBounds[1][0]..coordinateBounds[1][1]
    }

    //We assume that the target is in positive x and negative y direction
    override fun solvePart1(input: Pair<IntRange, IntRange>): Int {
        val maxYVelocity = maxYVelocity(input.second)

        val xVelocity = minXVelocityToHitWithTerminalX(input.first) ?: throw NotImplementedError("Only implemented for targets that can be hit with a terminal x position.")
        val minYVelocity = max(minYVelocityToHitWithTerminalX(input.second, xVelocity), 0)
        if (minYVelocity >= maxYVelocity){
            throw NotImplementedError("Only implemented for targets that can be hit with a terminal x position with max y velocity.")
        }

        return maxYPosition(maxYVelocity)
    }

    private fun minXVelocityToHitWithTerminalX(targetXRange: IntRange): Int?{
        //The terminal x position for initial velocity x0 is x0*(x0+1)/2, leading to the below bounds.
        val minVelocity = ceil(sqrt(2*targetXRange.first + 0.25) - 0.5).toInt()
        val maxVelocity = floor(sqrt(2*targetXRange.last + 0.25) - 0.5).toInt()
        return if (maxVelocity >= minVelocity) minVelocity else null
    }

    private fun minYVelocityToHitWithTerminalX(targetYRange: IntRange, xVelocity: Int): Int{
        //The terminal x position is reached after xVelocity steps.
        //At that point the y position must still be at or above the target.
        return ceil(xVelocity/2.0 + targetYRange.first/(1.0 + xVelocity)).toInt()
    }

    private fun maxYVelocity(targetYRange: IntRange): Int {
        //If we shoot upwards, we will reach level zero again after 2 * y0 steps.
        //At that point the y velocity is -(y0 + 1).
        //Accordingly, we cannot hit targets above -(y0 + 1).
        //Moreover, we hit -(y0 + 1) after 2 * y0 + 1 steps.
        return -targetYRange.first - 1
    }

    private fun maxYPosition(yVelocity: Int): Int {
        return yVelocity * (1 + yVelocity) / 2
    }


    override fun solvePart2(input: Pair<IntRange, IntRange>): Int {
        val possibleStartVelocities = possibleStartVelocities(input.first, input.second)
        return possibleStartVelocities.count()
    }

    //We assume that the target is in positive x and negative y direction
    private fun possibleStartVelocities(targetXRange: IntRange, targetYRange: IntRange): Set<Pair<Int, Int>>{
        val theoreticallyPossibleYVelocities = minYVelocity(targetYRange)..maxYVelocity(targetYRange)
        val hittingYVelocitiesWithHittingSteps = theoreticallyPossibleYVelocities.flatMap { yVelocity ->
            val hittingSteps = hittingSteps(targetYRange, yVelocity)
            hittingSteps.map { step -> yVelocity to step }
        }
        val xVelocitiesHittingAtTerminalX = minXVelocityToHitAtTerminalX(targetXRange)..maxXVelocityToHitAtTerminalX(targetXRange)
        return hittingYVelocitiesWithHittingSteps.flatMap { (yVelocity, step) ->
            val hittingXVelocities = xVelocitiesHittingInStep(targetXRange, step, xVelocitiesHittingAtTerminalX)
            hittingXVelocities.map { xVelocity -> xVelocity to yVelocity }
        }.toSet()
    }

    private fun minYVelocity(targetYRange: IntRange): Int {
        //If we already shoot down so fast that we land below the target after the first step, we miss it.
        //Moreover, it is always possible to simply shoot into the target after the first step.
        return targetYRange.first
    }

    private fun hittingSteps(targetYRange: IntRange, yVelocity: Int): List<Int>{
        val minHittingStep = ceil(yVelocity + 0.5 + sqrt((yVelocity + 0.5).pow(2) - 2 * targetYRange.last)).toInt()
        val maxHittingStep = floor(yVelocity + 0.5 + sqrt((yVelocity + 0.5).pow(2) - 2 * targetYRange.first)).toInt()
        return (minHittingStep..maxHittingStep).toList()
    }

    private fun minXVelocityToHitAtTerminalX(targetXRange: IntRange): Int{
        //The terminal x position for initial velocity x0 is x0*(x0+1)/2, leading to the below bound.
        return ceil(sqrt(2*targetXRange.first + 0.25) - 0.5).toInt()
    }

    private fun maxXVelocityToHitAtTerminalX(targetXRange: IntRange): Int{
        //The terminal x position for initial velocity x0 is x0*(x0+1)/2, leading to the below bound.
        return floor(sqrt(2*targetXRange.last + 0.25) - 0.5).toInt()
    }

    private fun xVelocitiesHittingInStep(targetXRange: IntRange, step: Int, xVelocitiesHittingAtTerminalX: IntRange): List<Int>{
        val hittingWithTerminalX = xVelocitiesHittingAtTerminalX.filter { it <= step }
        val minXVelocityHittingBeforeTerminalXInStep = max(step + 1, ceil(targetXRange.first.toDouble() / step + (step.toDouble() - 1) / 2).toInt())
        val maxXVelocityHittingBeforeTerminalXInStep = floor(targetXRange.last.toDouble() / step + (step.toDouble() - 1) / 2).toInt()
        val hittingBeforeTerminalX = (minXVelocityHittingBeforeTerminalXInStep..maxXVelocityHittingBeforeTerminalXInStep).toList()
        return hittingWithTerminalX + hittingBeforeTerminalX
    }
}