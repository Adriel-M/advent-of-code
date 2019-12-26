import java.io.File

fun getProgram(path: String): List<Int> {
    val unparsedProgram = File(path).readLines()
    return unparsedProgram[0].split(",").map { it.toInt() }
}

fun executeAdd(program: MutableList<Int>, currentIndex: Int) {
    val indexA = program[currentIndex + 1]
    val indexB = program[currentIndex + 2]
    val valA = program[indexA]
    val valB = program[indexB]
    val destination = program[currentIndex + 3]
    program[destination] = valA + valB
}

fun executeMult(program: MutableList<Int>, currentIndex: Int) {
    val indexA = program[currentIndex + 1]
    val indexB = program[currentIndex + 2]
    val valA = program[indexA]
    val valB = program[indexB]
    val destination = program[currentIndex + 3]
    program[destination] = valA * valB
}

fun runProgram(program: MutableList<Int>): Int {
    var currentIndex = 0
    loop@ while (program[currentIndex] != 99) {
        val currentOp = program[currentIndex]
        currentIndex += when (currentOp) {
            1 -> {
                executeAdd(program, currentIndex)
                4
            }
            2 -> {
                executeMult(program, currentIndex)
                4
            }
            else -> {
                println("Something broke")
                break@loop
            }
        }
    }
    return program[0]
}

fun runReplacedProgram(program: MutableList<Int>, noun: Int, verb: Int): Int {
    program[1] = noun
    program[2] = verb
    return runProgram(program)
}

fun runPart1(program: List<Int>): Int {
    return runReplacedProgram(program.toMutableList(), 12, 2)
}

fun runPart2(program: List<Int>): Int {
    for (noun in 0..99) {
        for (verb in 0..99) {
            if (runReplacedProgram(program.toMutableList(), noun, verb) == 19690720) {
                return (100 * noun) + verb
            }
        }
    }
    return -1
}

fun main() {
    val program = getProgram("../input")

    println("===== Part 1 =====")
    println(runPart1(program))
    println("===== Part 2 =====")
    println(runPart2(program))
}
