package adventOfCode2021.daySolutions


//The boards are so small that this implementation is probably more performant then one using a hash map.
class BingoBoard(val numbers: List<Int>, val boardWidth: Int){
    fun find(number: Int): Pair<Int,Int>?{
        val boardIndex = numbers.indexOf(number)
        return if (boardIndex == -1) null
            else (boardIndex / boardWidth) to (boardIndex % boardWidth)
    }
}

class Day4 : Day<Pair<List<Int>, List<BingoBoard>>, Int> {
    override fun parseInput(input: String): Pair<List<Int>, List<BingoBoard>> {
        val partSeparationRegEx = Regex("""\r?\n\r?\n""")
        val parts = input.split(partSeparationRegEx)
        val numbers = parseDrawnNumbers(parts[0])
        val boards = parts.drop(1).mapNotNull(::parseBingoBoard)
        return numbers to boards
    }

    private fun parseDrawnNumbers(text: String): List<Int> = text
        .split(',')
        .mapNotNull(String::toIntOrNull)

    private fun parseBingoBoard(text: String) : BingoBoard?{
        val rows = text.lines().map { it.split(' ').mapNotNull(String::toIntOrNull) }
        val width = rows[0].count()
        val numbers = rows.flatten()
        return BingoBoard(numbers, width)
    }

    override fun solvePart1(input: Pair<List<Int>, List<BingoBoard>>): Int {
        val (numbers, boards) = input
        val gameResult = playBingo(numbers, boards)
        if (gameResult == null){
            return -1
        }
        val (lastNumber, boardAndState) = gameResult
        val (winningBoard, winningBoardState) = boardAndState
        return score(lastNumber, winningBoard, winningBoardState)
    }

    private fun score(lastDrawnNumber: Int, board: BingoBoard, finalBoardState: BingoBoardState): Int{
        val markedNumberSum = finalBoardState.markedNumbers.sum()
        val boardNumberSum = board.numbers.sum()
        val unmarkedNumberSum = boardNumberSum - markedNumberSum
        return lastDrawnNumber * unmarkedNumberSum
    }

    /**
     * Plays a game of bingo
     *
     * @param numbers The list of numbers drawn in the game
     * @param boards The bingo boards taking part in the game
     * @return The last number drawn and the winning board together with its state, if there is a winner.
     * Returns null if the game runs out of numbers before there is a winner.
     */
    private fun playBingo(numbers: List<Int>, boards:List<BingoBoard>): Pair<Int,Pair<BingoBoard,BingoBoardState>>?{
        val boardsWithState = boards.map { it to BingoBoardState(it.boardWidth) }
        for(number in numbers){
            for((board, state) in boardsWithState){
                val numberCoordinates = board.find(number)
                if (numberCoordinates != null){
                    val completed = state.markNumber(numberCoordinates.first, numberCoordinates.second, number)
                    if (completed) {
                        return number to (board to state)
                    }
                }
            }
        }
        return null
    }

    override fun solvePart2(input: Pair<List<Int>, List<BingoBoard>>): Int {
        val (numbers, boards) = input
        val individualBoardWinStates = boards.mapNotNull { winState(it, numbers) }
        val lastWinningRound = individualBoardWinStates.maxOf { it.first }
        val lastWinningBoardWinState = individualBoardWinStates.last { it.first == lastWinningRound }
        val (_winningRound, lastDrawnNumber, boardAndState) = lastWinningBoardWinState
        val (board, boardState) = boardAndState
        return score(lastDrawnNumber, board, boardState)
    }

    private fun winState(board: BingoBoard, numbers: List<Int>): Triple<Int, Int, Pair<BingoBoard,BingoBoardState>>?{
        val (lastDrawnNumber, boardsWithState) = playBingo(numbers, listOf(board)) ?: return null
        val winningRound = numbers.indexOf(lastDrawnNumber) + 1
        return Triple(winningRound, lastDrawnNumber, boardsWithState)
    }

}

private class BingoBoardState(val boardWidth: Int){

    private val columnMarkedCounts = MutableList(boardWidth) { 0 }
    private val rowMarkedCounts = MutableList(boardWidth) { 0 }
    private val markedNumbersImpl = mutableSetOf<Int>()

    val markedNumbers: Set<Int> = markedNumbersImpl

    /**
     * Marks a coordinate on the board as drawn.
     *
     * @return Returns whether marking this coordinate completed a row or column.
     */
    fun markNumber(row: Int, column: Int, value: Int): Boolean{
        markedNumbersImpl.add(value)
        columnMarkedCounts[column]++
        rowMarkedCounts[row]++
        return columnMarkedCounts[column] == boardWidth || rowMarkedCounts[row] == boardWidth
    }
}