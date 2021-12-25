package adventOfCode2021.daySolutions

import adventOfCode2021.utility.Point2d


class Day25: Day<Pair<Pair<Set<Point2d>,Set<Point2d>>, Pair<Int, Int>>, Int> {
    override fun parseInput(input: String): Pair<Pair<Set<Point2d>, Set<Point2d>>, Pair<Int, Int>> {
        val lines = input.lines()
        val height = lines.count()
        val width = lines[0].length
        val rightMoving = extractOccupiedPoints(lines, '>')
        val downMoving = extractOccupiedPoints(lines, 'v')
        return (rightMoving to downMoving) to (width to height)
    }

    private fun extractOccupiedPoints(initialMap: List<String>, species: Char): Set<Point2d>{
        return initialMap.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                if(c==species) Point2d(x,y) else null
            }
        }.toSet()
    }

    override fun solvePart1(input: Pair<Pair<Set<Point2d>, Set<Point2d>>, Pair<Int, Int>>): Int {
        val (critters, dimensions) = input
        val (rightMoving, downMoving) = critters
        val (width, height) = dimensions
        return runTillStable(rightMoving.toMutableSet(), downMoving.toMutableSet(), width, height)
    }

    private fun runTillStable(rightMoving: MutableSet<Point2d>, downMoving: MutableSet<Point2d>, width: Int, height: Int): Int{
        var step = 0
        var hasMoved = true
        while (hasMoved){
            hasMoved = executeStep(rightMoving, downMoving, width, height)
            step++
        }
        return step
    }

    private fun executeStep(
        rightMoving: MutableSet<Point2d>,
        downMoving: MutableSet<Point2d>,
        width: Int,
        height: Int
    ): Boolean{
        val movingRight = rightMoving.filter {
            val target = rightMoveTarget(it, width)
            (target !in rightMoving) && (target !in downMoving)
        }.toSet()
        for (critter in movingRight){
            rightMoving.remove(critter)
            rightMoving.add(rightMoveTarget(critter, width))
        }

        val movingDown = downMoving.filter {
            val target = downMoveTarget(it, height)
            (target !in rightMoving) && (target !in downMoving)
        }.toSet()
        for (critter in movingDown){
            downMoving.remove(critter)
            downMoving.add(downMoveTarget(critter, height))
        }

        return movingDown.isNotEmpty() || movingRight.isNotEmpty()
    }

    private fun rightMoveTarget(point: Point2d, width: Int): Point2d = Point2d( (point.x + 1) % width, point.y)
    private fun downMoveTarget(point: Point2d, height: Int): Point2d = Point2d( point.x, (point.y + 1) % height)

    override fun solvePart2(input: Pair<Pair<Set<Point2d>, Set<Point2d>>, Pair<Int, Int>>): Int {
        TODO("Not yet implemented")
    }
}