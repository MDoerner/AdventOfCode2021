package adventOfCode2021.daySolutions


typealias Cavern = String
typealias CavernGraph = Map<Cavern, List<Cavern>>

private fun Cavern.isSmall(): Boolean = this.firstOrNull()?.isLowerCase() ?: true

private const val START_CAVERN = "start"
private const val END_CAVERN = "end"


class Day12 : Day<CavernGraph, Int> {
    override fun parseInput(input: String): CavernGraph {
        val edges = input.lines().mapNotNull(::parseEdge)
        val orientedEdges = edges + edges.map { it.second to it.first }
        return orientedEdges.groupBy(Pair<Cavern, Cavern>::first, Pair<Cavern, Cavern>::second)
    }

    private fun parseEdge(text: String): Pair<Cavern, Cavern>?{
        val parts = text.split('-')
        return if (parts.count() == 2) parts[0] to parts[1] else null
    }

    override fun solvePart1(input: CavernGraph): Int {
        val validNextCavernSelector = { cavern: Cavern, path: List<Cavern> -> !cavern.isSmall() || !path.contains(cavern) }
        val paths = pathsToEndCavern(listOf(START_CAVERN), input, validNextCavernSelector)
        return paths.count()
    }

    private fun pathsToEndCavern(startPath: List<Cavern>, cavernMap: CavernGraph, validNextCavernSelector: (Cavern, List<Cavern>) -> Boolean , endCavern: Cavern = END_CAVERN): List<List<Cavern>>{
        val currentCavern = startPath.last()
        if (currentCavern == endCavern) {
            return listOf(startPath)
        }
        val connectedCaverns = cavernMap[currentCavern] ?: return listOf()
        val validNextCaverns = connectedCaverns.filter { validNextCavernSelector(it, startPath)}
        return validNextCaverns
            .map { pathsToEndCavern( startPath + listOf(it), cavernMap, validNextCavernSelector, endCavern) }
            .flatten()
    }

    override fun solvePart2(input: CavernGraph): Int {
        val validNextCavernSelector = { cavern: Cavern, path: List<Cavern> ->
            !cavern.isSmall()
            || !path.contains(cavern)
            || (cavern != START_CAVERN
                && !containsDuplicate(path.filter(Cavern::isSmall)))
        }
        val paths = pathsToEndCavern(listOf(START_CAVERN), input, validNextCavernSelector)
        return paths.count()
    }

    private fun containsDuplicate(caverns: List<Cavern>): Boolean = caverns.count() > caverns.distinct().count()
}