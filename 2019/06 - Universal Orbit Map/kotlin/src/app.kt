import java.io.File
import java.util.*

fun main() {
    val edges = generateEdges("../input")

    println("===== Part 1 =====")
    println(runPart1(edges))
    println("===== Part 2 =====")
    println(runPart2(edges))
}

fun runPart1(edges: Map<String, Set<String>>): Int {
    val queue: Deque<Pair<String, Int>> = ArrayDeque()
    queue.addLast(Pair("COM", 0))

    val queuedNodes = mutableSetOf("COM")

    var numberOfOrbits = 0

    while (queue.size > 0) {
        val (currPlanet, currNumberOfOrbits) = queue.removeFirst()
        numberOfOrbits += currNumberOfOrbits
        edges[currPlanet]?.forEach {
            if (!queuedNodes.contains(it)) {
                queue.addLast(Pair(it, currNumberOfOrbits + 1))
                queuedNodes.add(it)
            }
        }
    }
    return numberOfOrbits
}

fun runPart2(edges: Map<String, Set<String>>): Int {
    return calculateMinimumOrbitalTransfers(edges, "YOU", "SAN")
}

fun calculateMinimumOrbitalTransfers(edges: Map<String, Set<String>>, source: String, destination: String): Int {
    val queue: Deque<String> = ArrayDeque()
    queue.addLast(source)

    val queuedNodes = mutableSetOf(source)
    var numberOfOrbitalTransfers = -2

    while (queue.size > 0) {
        for (i in 0 until queue.size) {
            val currentPlanet = queue.removeFirst()
            if (currentPlanet == destination) {
                return numberOfOrbitalTransfers
            }
            edges[currentPlanet]?.forEach {
                if (!queuedNodes.contains(it)) {
                    queue.addLast(it)
                    queuedNodes.add(it)
                }
            }
        }
        numberOfOrbitalTransfers += 1
    }
    return -1
}

fun generateEdges(path: String): Map<String, Set<String>> {
    val edges: MutableMap<String, MutableSet<String>> = mutableMapOf()
    File(path).forEachLine {
        val (node1, node2) = extractEdge(it)
        if (!edges.contains(node1)) {
            edges[node1] = mutableSetOf()
        }
        edges[node1]?.add(node2)
        if (!edges.contains(node2)) {
            edges[node2] = mutableSetOf()
        }
        edges[node2]?.add(node1)
    }
    return edges
}

fun extractEdge(line: String): Pair<String, String> {
    val splitLine = line.split(")")
    return Pair(splitLine[0], splitLine[1])
}
