import daySolutions.DaySolver
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import kotlin.test.fail
import kotlin.test.assertEquals

abstract class DayTestBase {

    abstract fun daySolverUnderTest(): DaySolver

    internal fun testOnDayInput(day: Int, part: Int, expectedResult: String){
        val input = testInputFromFile(day) ?: fail("Input does not exist!!!")
        testExampleInput(part, input, expectedResult)
    }

    internal fun testExampleInput(part: Int, exampleInput: String, expectedResult: String){
        val solver = daySolverUnderTest()
        val actualResult = when(part){
            1 -> solver.solutionForPart1(exampleInput)
            2 -> solver.solutionForPart2(exampleInput)
            else -> null
        } ?: fail("Unsupported part: $part")
        assertEquals(expectedResult, actualResult)
    }

    private fun testInputFromFile(day: Int): String?{
        val path = inputFilePath(day) ?: return null
        return problemInput(path)
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
}