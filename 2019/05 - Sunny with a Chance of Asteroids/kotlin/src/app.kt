import java.io.File
import kotlin.math.pow

fun main() {
    val instructions = getInstructions("../input")

    println("===== Part 1 ===== ")
    runPart1(instructions)
    println("===== Part 2 ===== ")
    runPart2(instructions)
}

fun runPart1(instructions: List<Int>) {
    val runner = ProgramRunner(instructions, 1)
    runner.runProgram()
}

fun runPart2(instructions: List<Int>) {
    val runner = ProgramRunner(instructions, 5)
    runner.runProgram()
}

fun getInstructions(path: String): List<Int> {
    val unparsedProgram = File(path).readLines()
    return unparsedProgram[0].split(",").map { it.toInt() }
}

class ProgramRunner(private val originalInstructions: List<Int>, private val inputValue: Int) {
    private val instructions = originalInstructions.toMutableList()
    private var currentIndex = 0

    fun runProgram() {
        while (currentIndex < instructions.size && getCurrentCommandType() != 99) {
            val nextIndex = getNextIndex()
            executeCommand()
            currentIndex = nextIndex
        }
    }

    private fun getCurrentCommandType(): Int {
        return instructions[currentIndex] % 100
    }

    private fun getModeForOffset(offset: Int): Int {
        val currentInstruction = instructions[currentIndex]
        val divValue = 10.toFloat().pow(offset + 1).toInt()
        return (currentInstruction / divValue) % 10
    }

    private fun executeCommand() {
        when (getCurrentCommandType()) {
            1 -> executeAddCommand()
            2 -> executeMultiplyCommand()
            3 -> executeInputCommand()
            4 -> executeOutputCommand()
            5, 6 -> noop()
            7 -> executeLessThanAndStore()
            8 -> executeEqualAndStore()
            else -> throw IllegalArgumentException()
        }
    }

    private fun executeAddCommand() {
        val a = getValue(currentIndex + 1, getModeForOffset(1))
        val b = getValue(currentIndex + 2, getModeForOffset(2))
        val targetIndex = getValue(currentIndex + 3, 1)
        instructions[targetIndex] = a + b
    }

    private fun executeMultiplyCommand() {
        val a = getValue(currentIndex + 1, getModeForOffset(1))
        val b = getValue(currentIndex + 2, getModeForOffset(2))
        val targetIndex = getValue(currentIndex + 3, 1)
        instructions[targetIndex] = a * b
    }

    private fun executeInputCommand() {
        val targetIndex = getValue(currentIndex + 1, 1)
        instructions[targetIndex] = inputValue
    }

    private fun executeOutputCommand() {
        val value = getValue(currentIndex + 1, getModeForOffset(1))
        println(value)
    }

    private fun noop() {}

    private fun executeLessThanAndStore() {
        val a = getValue(currentIndex + 1, getModeForOffset(1))
        val b = getValue(currentIndex + 2, getModeForOffset(2))
        val targetIndex = getValue(currentIndex + 3, 1)
        val valueToStore = if (a < b) 1 else 0
        instructions[targetIndex] = valueToStore
    }

    private fun executeEqualAndStore() {
        val a = getValue(currentIndex + 1, getModeForOffset(1))
        val b = getValue(currentIndex + 2, getModeForOffset(2))
        val targetIndex = getValue(currentIndex + 3, 1)
        val valueToStore = if (a == b) 1 else 0
        instructions[targetIndex] = valueToStore
    }

    private fun getValue(index: Int, mode: Int): Int {
        val currentValue = instructions[index]
        return when (mode) {
            0 -> instructions[currentValue]
            1 -> currentValue
            else -> throw IllegalArgumentException()
        }
    }

    private fun getNextIndex(): Int {
        return when (getCurrentCommandType()) {
            1, 2 -> currentIndex + 4
            3, 4 -> currentIndex + 2
            5 -> {
                val nextVal = getValue(currentIndex + 1, getModeForOffset(1))
                val possiblePosition = getValue(currentIndex + 2, getModeForOffset(2))
                if (nextVal != 0) {
                    possiblePosition
                } else {
                    currentIndex + 3
                }
            }
            6 -> {
                val nextVal = getValue(currentIndex + 1, getModeForOffset(1))
                val possiblePosition = getValue(currentIndex + 2, getModeForOffset(2))
                if (nextVal == 0) {
                    possiblePosition
                } else {
                    currentIndex + 3
                }
            }
            7, 8 -> currentIndex + 4
            else -> throw IllegalArgumentException()
        }
    }
}
