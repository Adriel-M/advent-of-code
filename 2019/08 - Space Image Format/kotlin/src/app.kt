import java.io.File
import java.nio.file.Path

fun main() {
    val digits = extractDigits("../input")

    println("===== Part 1 =====")
    println(runPart1(digits))
    println("===== Part 2 =====")
    println(runPart2(digits))
}

fun runPart1(digits: List<Int>): Int {
    val layers = partitionDigits(digits, 25 * 6)
    val layerDigitCounts = layers.map { generateCountsForPartition(it) }
    val layerWithFewestZero = layerDigitCounts.minBy {
        it[0] ?: -1
    } ?: return 0
    val onesCount = layerWithFewestZero[1] ?: 0
    val twosCount = layerWithFewestZero[2] ?: 2
    return onesCount * twosCount
}

fun runPart2(digits: List<Int>): String {
    val layers = partitionDigits(digits, 25 * 6)
    val image = (layers[0].indices).map { getColorForIndex(layers, it) }
    val imageFormatted = partitionDigits(image, 25)
    for (layer in imageFormatted) {
        println(layer)
    }
    return image.joinToString(separator="")
}

fun getColorForIndex(layers: List<List<Int>>, index: Int): Int {
    for (i in layers.indices) {
        if (layers[i][index] < 2) {
            return layers[i][index]
        }
    }
    return 2
}

fun extractDigits(path: String): List<Int> {
    val text = File(path).readText().trimEnd()
    return text.map { Character.getNumericValue(it) }
}

fun partitionDigits(digits: List<Int>, separateCount: Int): List<List<Int>> {
    val partitions: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
    digits.forEach {
        if (partitions.last().size == separateCount) {
            partitions.add(mutableListOf())
        }
        partitions.last().add(it)
    }
    return partitions
}

fun generateCountsForPartition(partition: List<Int>): Map<Int, Int> {
    return partition.groupingBy { it }.eachCount()
}

