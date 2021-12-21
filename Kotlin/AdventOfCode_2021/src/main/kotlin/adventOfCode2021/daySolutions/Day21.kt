package adventOfCode2021.daySolutions


private const val BOARD_LENGTH = 10
private const val DIRAC_WIN_SCORE = 21

//Note: All positions are moved one left relative to the problem statement to avoid subtracting and adding 1 everywhere.
class Day21: Day<Pair<Int,Int>, ULong> {
    override fun parseInput(input: String): Pair<Int, Int> {
        val startPositions = input.lines()
            .mapNotNull { it.drop("Player x starting position: ".length).toIntOrNull() }
        return (startPositions[0] - 1) to (startPositions[1] - 1)
    }

    override fun solvePart1(input: Pair<Int, Int>): ULong {
        val (round, losingScore) = playNaiveGame(input.first, input.second, 100, 1000, 10)
        return 3UL * round.toULong() * losingScore.toULong()
    }

    //Returns the winning round and losing score.
    private fun playNaiveGame(player1start: Int, player2start: Int, die: Int, stopScore: Int, boardSlots: Int): Pair<Int, Int>{
        var roundIndex = 0  //Again, we want to avoid subtracting and adding 1 everywhere.
        var player1 = player1start
        var player2 = player2start
        var player1Score = 0
        var player2Score = 0
        while (player1Score < stopScore && player2Score < stopScore){
            if (roundIndex % 2 == 0){
                player1 = position(player1,  movement(roundIndex, die), boardSlots)
                player1Score += moveScore(player1)
            } else {
                player2 = position(player2,  movement(roundIndex, die), boardSlots)
                player2Score += moveScore(player2)
            }
            roundIndex++
        }
        return roundIndex to if(player1Score >= stopScore) player2Score else player1Score
    }

    private fun movement(roundIndex:Int, die:Int): Int = 3 * ((3 * roundIndex) % die) + 6
    private fun position(priorPosition: Int, movement: Int, boardSlots: Int): Int = (priorPosition + movement) % boardSlots
    private fun moveScore(position: Int): Int = (position + 1)

    override fun solvePart2(input: Pair<Int, Int>): ULong {
        val (player1WinCount, player2WinCount) = playDiracGame(input.first, input.second)
        return if (player1WinCount > player2WinCount) player1WinCount else player2WinCount
    }

    private fun playDiracGame(player1InitialPosition: Int, player2InitialPosition: Int): Pair<ULong, ULong>{
        var possibleNonEndedPlayer1StatesWithCounts = mapOf( (player1InitialPosition to 0) to 1UL )
        var possibleNonEndedPlayer2StatesWithCounts = mapOf( (player2InitialPosition to 0) to 1UL )
        var activePlayer1StatesCount = 1UL
        var activePlayer2StatesCount = 1UL
        var player1WinStateCount = 0UL
        var player2WinStateCount = 0UL

        while (possibleNonEndedPlayer1StatesWithCounts.isNotEmpty() && possibleNonEndedPlayer2StatesWithCounts.isNotEmpty()){
            val (newPlayer1WinCounts, newNonWinPlayer1States) =
                playDiracRound(possibleNonEndedPlayer1StatesWithCounts, activePlayer2StatesCount)
            player1WinStateCount += newPlayer1WinCounts
            possibleNonEndedPlayer1StatesWithCounts = newNonWinPlayer1States
            activePlayer1StatesCount = possibleNonEndedPlayer1StatesWithCounts.values.sum()
            val (newPlayer2WinCounts, newNonWinPlayer2States) =
                playDiracRound(possibleNonEndedPlayer2StatesWithCounts, activePlayer1StatesCount)
            player2WinStateCount += newPlayer2WinCounts
            possibleNonEndedPlayer2StatesWithCounts = newNonWinPlayer2States
            activePlayer2StatesCount = possibleNonEndedPlayer2StatesWithCounts.values.sum()
        }
        return player1WinStateCount to player2WinStateCount
    }

    //Returns the number of new win states and returns the new non-win states with their counts.
    private fun playDiracRound(playerStatesWithCounts: Map<Pair<Int,Int>, ULong>, opponentStateCount: ULong): Pair<ULong, Map<Pair<Int,Int>, ULong>>{
        val positionsAfterMove = playerStatesWithCounts.flatMap { (state, count) ->
            val (position, score) = state
            diracMoves.map {
                val newPosition = position(position, it.first, BOARD_LENGTH)
                val newScore = score + moveScore(newPosition)
                val newCount = count * it.second
                ( newPosition to newScore ) to newCount
            }
        }
            .groupBy( { it.first }, { it.second })
            .mapValues { it.value.sum() }
        val winStateCount = positionsAfterMove
            .filterKeys { (_position, score) -> score >= DIRAC_WIN_SCORE }
            .values
            .sum()
        val nonWinStates = positionsAfterMove
            .filterKeys { (_position, score) -> score < DIRAC_WIN_SCORE }
        return opponentStateCount * winStateCount to nonWinStates
    }

    //Possible values after throwing three dice and number of ways to do it.
    private val diracMoves = listOf(
        3 to 1UL,
        4 to 3UL,
        5 to 6UL,
        6 to 7UL,
        7 to 6UL,
        8 to 3UL,
        9 to 1UL,
    )
}