import daySolutions.DaySolver
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import kotlin.test.fail
import kotlin.test.assertEquals

abstract class DayTestBase {

    abstract fun DaySolverUnderTest(): DaySolver

    internal fun TestOnDayInput(day: Int, part: Int, expectedResult: String){
        val input = TestInputFromFile(day) ?: fail("Input does not exist!!!")
        TestExampleInput(part, input, expectedResult)
    }

    internal fun TestExampleInput(part: Int, exampleInput: String, expectedResult: String){
        val solver = DaySolverUnderTest()
        val actualResult = when(part){
            1 -> solver.solutionForPart1(exampleInput)
            2 -> solver.solutionForPart2(exampleInput)
            else -> null
        } ?: fail("Unsupported part: ${part}")
        assertEquals(expectedResult, actualResult)
    }

    private fun TestInputFromFile(day: Int): String?{
        val path = inputFilePath(day) ?: return null
        return problemInput(path)
    }

    private fun problemInput(inputPath: String): String?{
        val reader = File(inputPath).bufferedReader()
        return reader.use { it.readText() }
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