import daySolutions.DaySolver
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import kotlin.NumberFormatException

fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")

    val (day, part) = parseArguments(args) ?: return
    println("Problem specification: day ${day}, part $part")

    val solver = problemSolver(day) ?: return
    println("Problem solver: $solver")

    val inputPath = inputFilePath(day) ?: return
    println("Input file: $inputPath")

    val input = problemInput(inputPath) ?: return

    val solution = if (part == 1) solver.solutionForPart1(input) else solver.solutionForPart2(input)
    println("Solution:\n${solution}")
}

private fun parseArguments(args: Array<String>): Pair<Int, Int>?{
    if (args.count() < 2) return null

    val day = try {args[0].toInt()} catch(e:NumberFormatException) {null}
    if (day == null || day < 0 || day > 25) return null

    val part = try {args[1].toInt()} catch(e:NumberFormatException) {null}
    if (part == null || part < 1 || part > 2) return null

    return day to part
}

private fun problemInput(inputPath: String): String?{
    return try{
        val reader = File(inputPath).bufferedReader()
        reader.use { it.readText() }
    }
    catch (e: Exception){
        null
    }
}

private fun inputFilePath(day: Int):String?{
    val filename = "Day${day}.txt"
    return try{
        Paths.get("").toAbsolutePath().parent.parent.resolve("Input").resolve(filename).toString()
    } catch (e : InvalidPathException) {
        null
    }
}

private fun problemSolver(day: Int): DaySolver?{
    val solver = try {
        Class.forName("daySolutions.Day${day}").constructors.single().newInstance()
    }catch (e : Exception)
    {
        null
    }
    return solver as DaySolver?
}