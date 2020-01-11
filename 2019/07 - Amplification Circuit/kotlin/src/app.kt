import java.io.File
import kotlin.math.pow

fun main() {
    val instructions = getInstructions("../input2")

//    println("===== Part 1 ===== ")
//    println(runPart1(instructions))
    println("===== Part 2 ===== ")
    println(runPart2(instructions))
}

fun runPart1(instructions: List<Int>): Int {
    val iter = PermutationIterator(0, 4)
    var bestAmplification = -1
    while (iter.hasNext()) {
        var previousOutput = 0
        for (i in iter.next()) {
            val runner = ProgramRunner(instructions, listOf(i, previousOutput))
            runner.runProgram()
            previousOutput = runner.getOutputValue()
        }
        if (previousOutput > bestAmplification) {
            bestAmplification = previousOutput
        }
    }
    return bestAmplification
}

fun runPart2(instructions: List<Int>): Int {
    val iter1 = PermutationIterator(0, 4)
    var bestAmplification = -1
    while (iter1.hasNext()) {
        var previousOutput = 0
        for (i in iter1.next()) {
            val runner = ProgramRunner(instructions, listOf(i, previousOutput))
            runner.runProgram()
            previousOutput = runner.getOutputValue()
        }
        val wow = listOf(9, 8, 7, 6, 5)
        for (i in wow) {
            val runner = ProgramRunner(instructions, listOf(i, previousOutput))
            runner.runProgram()
            previousOutput = runner.getOutputValue()
        }
        if (previousOutput > bestAmplification) {
            bestAmplification = previousOutput
        }
//        val iter2 = PermutationIterator(5, 9)
//        while (iter2.hasNext()) {
//            for (i in iter2.next()) {
//                val runner = ProgramRunner(instructions, listOf(i, previousOutput))
//                runner.runProgram()
//                previousOutput = runner.getOutputValue()
//            }
//            if (previousOutput > bestAmplification) {
//                bestAmplification = previousOutput
//            }
//        }
    }
    return bestAmplification
}

fun getInstructions(path: String): List<Int> {
    val unparsedProgram = File(path).readLines()
    return unparsedProgram[0].split(",").map { it.toInt() }
}

class PermutationIterator(private val minValue: Int, private val maxValue: Int): Iterator<List<Int>> {
    private var currentState: MutableList<Int> = mutableListOf()
    private val finalState = (maxValue downTo minValue).toList()

    override fun hasNext(): Boolean {
        return currentState != finalState
    }

    override fun next(): List<Int> {
        if (currentState.size == 0) {
            currentState = (minValue..maxValue).toMutableList()
        } else {
            generateNextPermutation()
        }
        return currentState
    }

    private fun generateNextPermutation() {
        val decreasingIndex = findDecreasingIndexFromRight()
        if (decreasingIndex < 0) {
            reverse(0, currentState.size - 1)
            return
        }
        var targetIndex = currentState.size - 1
        while (targetIndex >= 0 && currentState[targetIndex] < currentState[decreasingIndex]) {
            targetIndex--
        }
        swap(decreasingIndex, targetIndex)
        swap(decreasingIndex + 1, currentState.size - 1)
    }

    private fun findDecreasingIndexFromRight(): Int {
        var index = currentState.size - 2
        while (index >= 0 && currentState[index + 1] < currentState[index]) {
            index--
        }
        return index
    }

    private fun reverse(start: Int, end: Int) {
        var i = start
        var j = end
        while (i < j) {
            swap(i, j)
            i++
            j--
        }
    }

    private fun swap(firstIndex: Int, secondIndex: Int) {
        val tmp = currentState[firstIndex]
        currentState[firstIndex] = currentState[secondIndex]
        currentState[secondIndex] = tmp
    }
}

class ProgramRunner(private val originalInstructions: List<Int>, private val inputValues: List<Int>) {
    private val instructions = originalInstructions.toMutableList()
    private var currentIndex = 0
    private var inputIndex = 0
    private var outputValue = -1

    fun getOutputValue(): Int {
        return outputValue
    }

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
        instructions[targetIndex] = inputValues[inputIndex]
        inputIndex++
    }

    private fun executeOutputCommand() {
        outputValue = getValue(currentIndex + 1, getModeForOffset(1))
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
            3 -> currentIndex + 2
            4 -> instructions.size
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
