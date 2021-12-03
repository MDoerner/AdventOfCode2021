package adventOfCode2021.daySolutions


typealias DiagnosticNumber = List<Int>

class Day3 : Day<List<DiagnosticNumber>, Int> {
    override fun parseInput(input: String): List<DiagnosticNumber> {
        val maybeDiagnostics = input.lines().mapNotNull(::parseDiagnosticNumber)
        val diagnosticLength = maybeDiagnostics[0].count()
        return maybeDiagnostics.filter { it.count() == diagnosticLength }
    }

    private fun parseDiagnosticNumber(diagnosticText: String): DiagnosticNumber?{
        if(!diagnosticText.all { it == '1' || it == '0' }){
            return null
        }
        return diagnosticText.map { it.digitToInt() }
    }

    override fun solvePart1(input: List<DiagnosticNumber>): Int {
        val gamma = gammaRate(input)
        val epsilon = epsilonRate(input)
        val gammaValue = toInt(gamma)
        val epsilonValue = toInt(epsilon)
        return gammaValue * epsilonValue
    }

    private fun gammaRate(diagnostics: List<DiagnosticNumber>): DiagnosticNumber{
        val numberOfDiagnosticBits = diagnostics[0].count()
        return (0 until numberOfDiagnosticBits).map { mostCommonBit(diagnostics, it) }
    }

    private fun mostCommonBit(diagnostics: List<DiagnosticNumber>, index: Int, tieValue: Int = 1): Int{
        val numberOfDiagnostics = diagnostics.count()
        val numberOfSetBits = diagnostics.sumOf { it[index] }

        return if(2 * numberOfSetBits > numberOfDiagnostics) 1
            else if(2 * numberOfSetBits < numberOfDiagnostics) 0
            else tieValue
    }

    private fun epsilonRate(diagnostics: List<DiagnosticNumber>): DiagnosticNumber{
        val numberOfDiagnosticBits = diagnostics[0].count()
        return (0 until numberOfDiagnosticBits).map { leastCommonBit(diagnostics, it) }
    }

    private fun leastCommonBit(diagnostics: List<DiagnosticNumber>, index: Int, tieValue: Int = 0): Int{
        val numberOfDiagnostics = diagnostics.count()
        val numberOfSetBits = diagnostics.sumOf { it[index] }

        return if(numberOfSetBits == numberOfDiagnostics) 1
        else if(numberOfSetBits == 0) 0
        else if(2 * numberOfSetBits < numberOfDiagnostics) 1
        else if(2 * numberOfSetBits < numberOfDiagnostics) 0
        else tieValue
    }

    private fun toInt(diagnostic: DiagnosticNumber): Int{
        val (_exceedingBit, result) = diagnostic
            .reversed()
            .fold(1 to 0) { (bitValue, value), bit -> (bitValue shl 1) to value + bit * bitValue }
        return result
    }


    override fun solvePart2(input: List<DiagnosticNumber>): Int {
        val oxygenRating = oxygenGenerationRatings(input)
        val co2Rating = co2ScrubberRatings(input)
        val oxygenRatingValue = toInt(oxygenRating)
        val co2RatingValue = toInt(co2Rating)
        return oxygenRatingValue * co2RatingValue
    }

    private fun oxygenGenerationRatings(diagnostics: List<DiagnosticNumber>): DiagnosticNumber{
        val possibleRatings = applyBitCriteria(diagnostics) { diags, bitIndex ->  mostCommonBit(diags, bitIndex, 1)}
        return possibleRatings.single()
    }

    private fun applyBitCriteria(diagnostics: List<DiagnosticNumber>, bitSelector: (List<DiagnosticNumber>, Int) -> Int): List<DiagnosticNumber>{
        val numberOfDiagnosticBits = diagnostics[0].count()
        return (0 until numberOfDiagnosticBits).fold(diagnostics) { remainingDiagnostics, bitIndex ->
            val filterBit = bitSelector(remainingDiagnostics, bitIndex)
            remainingDiagnostics.filter { it[bitIndex] == filterBit }
        }
    }

    private fun co2ScrubberRatings(diagnostics: List<DiagnosticNumber>): DiagnosticNumber{
        val possibleRatings = applyBitCriteria(diagnostics) { diags, bitIndex ->  leastCommonBit(diags, bitIndex, 0)}
        return possibleRatings.single()
    }
}