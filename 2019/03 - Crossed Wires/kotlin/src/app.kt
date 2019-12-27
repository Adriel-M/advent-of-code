import java.io.File
import kotlin.math.absoluteValue

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}

data class Move(val direction: Direction, val magnitude: Int)

fun extractMovesFromString(moveString: String): List<Move> {
    return moveString.split(",").map {
        val direction = when (it[0]) {
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> Direction.UP
        }
        val magnitude = it.substring(1).toInt()
        Move(direction, magnitude)
    }
}

fun getWireMoves(path: String): Pair<List<Move>, List<Move>> {
    val lines = File(path).readLines()
    return Pair(
        extractMovesFromString(lines[0]),
        extractMovesFromString(lines[1])
    )
}

fun getCoordinatesFromMoves(moves: List<Move>): Set<Pair<Int, Int>> {
    val coordinates: MutableSet<Pair<Int, Int>> = mutableSetOf()
    var currX = 0
    var currY = 0
    for (move in moves) {
        for (x in 0 until move.magnitude) {
            currX += move.direction.dx
            currY += move.direction.dy
            coordinates.add(Pair(currX, currY))
        }
    }
    return coordinates.toSet()
}

fun getDistancesFromMoves(moves: List<Move>): Map<Pair<Int, Int>, Int> {
    val distances: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    var currX = 0
    var currY = 0
    var currDistance = 0
    for (move in moves) {
        for (x in 0 until move.magnitude) {
            currX += move.direction.dx
            currY += move.direction.dy
            currDistance += 1
            val key = Pair(currX, currY)
            if (!distances.containsKey(key)) {
                distances[key] = currDistance
            }
        }
    }
    return distances.toMap()
}

fun getManhattanDistance(x: Int, y: Int): Int {
    return x.absoluteValue + y.absoluteValue
}

fun part1(moves1: List<Move>, moves2: List<Move>): Int {
    val coords1 = getCoordinatesFromMoves(moves1)
    val coords2 = getCoordinatesFromMoves(moves2)
    var closestIntersection = Int.MAX_VALUE
    for ((x, y) in coords1 intersect coords2) {
        val manhattanDistance = getManhattanDistance(x, y)
        if (manhattanDistance < closestIntersection) {
            closestIntersection = manhattanDistance
        }
    }
    return closestIntersection
}

fun part2(moves1: List<Move>, moves2: List<Move>): Int {
    val coords1 = getCoordinatesFromMoves(moves1)
    val coords2 = getCoordinatesFromMoves(moves2)
    val distances1 = getDistancesFromMoves(moves1)
    val distances2 = getDistancesFromMoves(moves2)
    var closestDistance = Int.MAX_VALUE
    for ((x, y) in coords1 intersect coords2) {
        val key = Pair(x, y)
        val d1 = distances1[key]
        val d2 = distances2[key]
        if (d1 == null || d2 == null) {
            continue
        }
        val totalDistance = d1 + d2
        if (totalDistance < closestDistance) {
            closestDistance = totalDistance
        }
    }
    return closestDistance
}

fun main() {
    val (moves1, moves2) = getWireMoves("../input")

    println("===== Part 1 =====")
    println(part1(moves1, moves2))

    println("===== Part 2 =====")
    println(part2(moves1, moves2))
}
