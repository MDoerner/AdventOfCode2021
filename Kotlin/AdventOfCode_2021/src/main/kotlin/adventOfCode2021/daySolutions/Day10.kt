package adventOfCode2021.daySolutions

import adventOfCode2021.utility.MutableStack
import adventOfCode2021.utility.toList


class Day10 : Day<List<String>, ULong> {
    override fun parseInput(input: String): List<String> {
        return input.lines().filter(String::isNotEmpty)
    }

    override fun solvePart1(input: List<String>): ULong {
        val corruptions = input.mapNotNull(::firstCorruption)
        return corruptions.sumOf { pointsByCorruption.getOrDefault(it, 0UL) }
    }

    private fun firstCorruption(line: String): Char?{
        val scopeOpeners = MutableStack<Char>()
        for (c in line){
            if (closingByOpening.containsKey(c)){
                scopeOpeners.push(c)
            } else {
                val currentScopeOpener = scopeOpeners.pop() ?: return c
                val scopeClosing = closingByOpening[currentScopeOpener]
                if (c != scopeClosing){
                    return c
                }
            }
        }
        return null
    }

    private val closingByOpening = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    private val pointsByCorruption = mapOf(
        ')' to 3UL,
        ']' to 57UL,
        '}' to 1197UL,
        '>' to 25137UL,
    )

    override fun solvePart2(input: List<String>): ULong {
        val nonCorruptedLines = input.filter { firstCorruption(it) == null }
        val missingEnds = nonCorruptedLines
            .map(::missingClosings)
            .filter(List<Char>::isNotEmpty)
        val scores = missingEnds.map(::missingEndScore)
        return if (scores.isEmpty()) 0UL else scores.sorted()[(scores.count() - 1) / 2]
    }

    //Expects non-corrupted line.
    private fun missingClosings(line: String): List<Char>{
        val scopeOpeners = MutableStack<Char>()
        for (c in line){
            if (closingByOpening.containsKey(c)){
                scopeOpeners.push(c)
            } else {
                val _scopeOpener = scopeOpeners.pop()
            }
        }

        return scopeOpeners
            .toList()
            .mapNotNull { closingByOpening[it] }
    }

    private fun missingEndScore(missingEnd: List<Char>): ULong{
        return missingEnd.fold(0UL) { score, c -> score * 5UL + scoreByEndChar.getOrDefault(c,0UL) }
    }

    private val scoreByEndChar = mapOf(
        ')' to 1UL,
        ']' to 2UL,
        '}' to 3UL,
        '>' to 4UL,
    )
}