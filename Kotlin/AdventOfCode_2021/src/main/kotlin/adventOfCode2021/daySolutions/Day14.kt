package adventOfCode2021.daySolutions


data class InsertionRule(val startElement: Char, val endElement: Char, val insertElement: Char)
data class PolymerTemplate(val template: String, val rules: List<InsertionRule>)

class Day14 : Day<PolymerTemplate, ULong> {
    override fun parseInput(input: String): PolymerTemplate {
        val partSeparationRegEx = Regex("""\r?\n\r?\n""")
        val parts = input.split(partSeparationRegEx)
        val rules = parts[1]
            .lines()
            .mapNotNull(::parseInsertionRule)
        return PolymerTemplate(parts[0], rules)
    }

    private fun parseInsertionRule(text: String): InsertionRule?{
        val ruleParts = text.split(" -> ")
        if (ruleParts.count() != 2) {
            return null
        }
        val insertionElement = ruleParts[1]
        if (insertionElement.length != 1) {
            return null
        }
        val pattern = ruleParts[0]
        if (pattern.length != 2) {
            return null
        }
        return InsertionRule(pattern[0], pattern[1], insertionElement[0])
    }

    override fun solvePart1(input: PolymerTemplate): ULong {
        val polymer = performReplacements(input.template, input.rules, 10)
        val elementCounts = polymer
            .groupBy { it }
            .values
            .map(List<Char>::count)
        return (elementCounts.maxOf { it } - elementCounts.minOf { it }).toULong()
    }

    fun performReplacements(template: String, rules: List<InsertionRule>, steps: Int): String{
        val ruleMap = rules
            .groupBy(InsertionRule::startElement)
            .map { it.key to it.value.associate { rule -> rule.endElement to rule.insertElement } }
            .toMap()
        return (1..steps).fold(template) { currentPolymer, _ -> performReplacement(currentPolymer, ruleMap) }
    }

    private fun performReplacement(polymer: String, ruleMap: Map<Char,Map<Char,Char>>): String{
        val polymerBuilder = polymer.windowed(2).fold(StringBuilder()) { currentPolymer, elements ->
            currentPolymer.append(elements[0])
            val insertionElement = ruleMap[elements[0]]?.get(elements[1])
            if (insertionElement != null){
                currentPolymer.append(insertionElement)
            }
            currentPolymer
        }
        polymerBuilder.append(polymer.last())
        return polymerBuilder.toString()
    }

    override fun solvePart2(input: PolymerTemplate): ULong {
        val counts = elementCounts(input, 40)
        return (counts.maxOf { it.value } - counts.minOf { it.value })
    }

    fun elementCounts(template: PolymerTemplate, steps: Int): Map<Char, ULong>{
        val memory = initialElementCountMemory(template)
        val ruleMap = template.rules
            .groupBy(InsertionRule::startElement)
            .map { it.key to it.value.associate { rule -> rule.endElement to rule.insertElement } }
            .toMap()

        val countsForPairs = template.template.windowed(2).map { elementCountForPair(it[0], it[1], steps, ruleMap, memory) }
        val elementCounts = countsForPairs
            .flatMap { it.asSequence() }
            .groupBy( { it.key }, { it.value })
            .map { it.key to it.value.sum() }
            .toMap()
            .toMutableMap()

        val lastElement = template.template.last()
        val currentLastElementCount = elementCounts[lastElement]
        if (currentLastElementCount == null){
            elementCounts[lastElement] = 1UL
        } else {
            elementCounts[lastElement] = currentLastElementCount + 1UL
        }

        return elementCounts
    }

    private fun initialElementCountMemory(template: PolymerTemplate): Map<Pair<Char, Char>,MutableMap<Int,Map<Char, ULong>>>{
        val existingElements = (template.template.asSequence() + template.rules.map { it.insertElement }).toSet()
        val existingPairs = existingElements
            .map { firstElement -> existingElements. map { firstElement to it } }
            .flatten()
            .distinct()
        return existingPairs.associateWith { mutableMapOf() }
    }

    //Does not count the last element.
    private fun elementCountForPair(
        startElement: Char,
        nextElement: Char,
        steps: Int,
        ruleMap: Map<Char,Map<Char,Char>>,
        memory: Map<Pair<Char, Char>, MutableMap<Int,Map<Char, ULong>>>): Map<Char, ULong>
    {
        val memorizedResult = memory[startElement to nextElement]?.get(steps)
        if (memorizedResult != null){
            return memorizedResult
        }

        if (steps <= 0){
            val baseResult = mapOf(startElement to 1UL)
            memory[startElement to nextElement]?.set(steps, baseResult)
            return baseResult
        }

        val insertionElement = ruleMap[startElement]?.get(nextElement)
        if (insertionElement == null){
            val baseResult = mapOf(startElement to 1UL)
            memory[startElement to nextElement]?.set(steps, baseResult)
            return baseResult
        }

        val startCounts = elementCountForPair(startElement, insertionElement, steps - 1, ruleMap, memory)
        val endCounts = elementCountForPair(insertionElement, nextElement, steps - 1, ruleMap, memory)

        val elementCounts = listOf(startCounts, endCounts)
            .flatMap { it.asSequence() }
            .groupBy( { it.key }, { it.value })
            .map { it.key to it.value.sum() }
            .toMap()
        memory[startElement to nextElement]?.set(steps, elementCounts)
        return elementCounts
    }
}