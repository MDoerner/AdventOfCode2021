package adventOfCode2021.daySolutions


class Day6 : Day<List<Int>, ULong> {
    override fun parseInput(input: String): List<Int>{
        return input.split(',').mapNotNull(String::toIntOrNull)
    }

    override fun solvePart1(input: List<Int>): ULong {
        return populationAfterDays(input, 80)
    }

    private fun populationAfterDays(birthingDelays: List<Int>, numberOfDays: Int): ULong{
        val initialPopulation = initialBirthingBuckets(birthingDelays)
        val adultCountByBirthingBucket = initialPopulation.first
        var freshJuvenileCount = initialPopulation.second
        var seniorJuvenileCount = initialPopulation.third
        for (day in 0 until numberOfDays){
            val todaysBucket = day % 7
            val newBornCount = adultCountByBirthingBucket[todaysBucket]
            adultCountByBirthingBucket[todaysBucket] += seniorJuvenileCount
            seniorJuvenileCount = freshJuvenileCount
            freshJuvenileCount = newBornCount
        }
        return freshJuvenileCount + seniorJuvenileCount + adultCountByBirthingBucket.sum()
    }

    private fun initialBirthingBuckets(birthingDelays: List<Int>): Triple<MutableList<ULong>, ULong, ULong>{
        val freshJuvenileCount = birthingDelays
            .count {it == 8}
            .toULong()
        val seniorJuvenileCount = birthingDelays
            .count {it == 7}
            .toULong()
        val adultsByBucket =  birthingDelays
            .filter {it <= 6}
            .groupBy {it % 7}
        val bucketPopulations  = (0..6).map { adultsByBucket[it]?.count()?.toULong() ?: 0UL }.toMutableList()
        return Triple(bucketPopulations, seniorJuvenileCount, freshJuvenileCount)
    }

    override fun solvePart2(input: List<Int>): ULong {
        return populationAfterDays(input, 256)
    }
}