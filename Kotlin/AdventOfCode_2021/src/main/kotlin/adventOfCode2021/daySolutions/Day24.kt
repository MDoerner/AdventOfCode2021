package adventOfCode2021.daySolutions

import adventOfCode2021.utility.MutableStack

data class MonadStepParameters(val base: Long, val conditionOffset: Long, val valueOffset: Long)

class Day24: Day<List<MonadStepParameters>, Long> {
    override fun parseInput(input: String): List<MonadStepParameters> {
        val commands = input.lines()
        val monadSteps = commands.chunked(commands.count()/14)
        return monadSteps.map(::extractParameters)
    }

    private fun extractParameters(monadStepCode: List<String>): MonadStepParameters{
        val base = monadStepCode[4].drop("div z ".length).toLong()
        val conditionOffset = monadStepCode[5].drop("add y ".length).toLong()
        val valueOffset = monadStepCode[15].drop("add y ".length).toLong()
        return MonadStepParameters(base, conditionOffset, valueOffset)
    }

    //This was a code analysis challenge.
    //The input describes a program as presented below in monad.
    //The parameters are chosen such that there are 7 with base 1 and 7 with base 26.
    //For all those parameters with base 1, the condition offset is between 10 and 16.
    //Accordingly, it is impossible to satisfy the if-condition with an input in the range 1..9.
    //Consequently, to reach a result of zero at the end, all 7 invocations of monadStep with base 26 have to take the if branch.

    override fun solvePart1(input: List<MonadStepParameters>): Long {
        val (referencedAssocs, referencingAssocs) = parameterAssociations(input)
        val maxValues = (0 until input.count())
            .fold(mutableListOf<Long>()) { digits, index ->
                val referencedOffset = referencedAssocs[index]
                val nextDigit = if (referencedOffset != null){
                    if (referencedOffset > 0L) 9L - referencedOffset else 9L
                } else {
                    val referencingOffset = referencingAssocs[index]!!
                    if (referencingOffset < 0L) 9L + referencingOffset else 9L
                }
                digits.add(nextDigit)
                digits
            }
        return maxValues.joinToString("").toLong()
    }

    private fun parameterAssociations(parameters: List<MonadStepParameters>): Pair<Map<Int, Long>, Map<Int, Long>>{
        val assocs = inputAssociationsWithOffset(parameters)
        val referencedAssocs = assocs
            .map { it.first to it.third }
            .toMap()
        val referencingAssocs = assocs
            .map { it.second to it.third }
            .toMap()
        return  referencedAssocs to referencingAssocs
    }

    private fun inputAssociationsWithOffset(parameters: List<MonadStepParameters>): List<Triple<Int, Int, Long>>{
        val associations = mutableListOf<Triple<Int, Int, Long>>()
        val inputStack = MutableStack<Pair<Int, Long>>()
        for ((index, parameter) in parameters.withIndex()){
            if(parameter.base == 1L){
                inputStack.push(index to parameter.valueOffset)
            } else {
                val (referencedIndex, valueOffset) = inputStack.pop()!!
                val offset = parameter.conditionOffset + valueOffset
                val association = Triple(referencedIndex, index, offset)
                associations.add(association)
            }
        }
        return associations
    }



    override fun solvePart2(input: List<MonadStepParameters>): Long {
        val (referencedAssocs, referencingAssocs) = parameterAssociations(input)
        val minValues = (0 until input.count())
            .fold(mutableListOf<Long>()) { digits, index ->
                val referencedOffset = referencedAssocs[index]
                val nextDigit = if (referencedOffset != null){
                    if (referencedOffset < 0L) 1L - referencedOffset else 1L
                } else {
                    val referencingOffset = referencingAssocs[index]!!
                    if (referencingOffset > 0L) 1L + referencingOffset else 1L
                }
                digits.add(nextDigit)
                digits
            }
        return minValues.joinToString("").toLong()
    }


    private fun monad(parameters: List<MonadStepParameters>, input: List<Long>): Long{
        val parameterInputPairs = parameters.zip(input)
        return parameterInputPairs.fold(0L) { z, (parameter, input) -> monadStep(parameter, input, z) }
    }

    private fun monadStep(parameters: MonadStepParameters, input:Long, z: Long): Long {
        val reducedZ = z / parameters.base
        return if ((z % 26L) + parameters.conditionOffset == input){
            reducedZ
        } else {
            26L * reducedZ + (input + parameters.valueOffset)
        }
    }
}