import java.io.File

fun calculateRequiredFuel1(mass: Int): Int {
    return (mass / 3) - 2;
}

fun calculateRequiredFuel2(mass: Int): Int {
    var total = 0;
    var currentMass = mass;
    while (currentMass > 0) {
        val requiredFuel = calculateRequiredFuel1(currentMass)
        if (requiredFuel > 0) {
            total += requiredFuel
        }
        currentMass = requiredFuel
    }
    return total
}

fun main() {
    val masses: MutableList<Int> = mutableListOf()
    File("../input").forEachLine {masses.add(it.toInt())}

    var part1Fuel = 0
    var part2Fuel = 0
    for (mass in masses) {
        part1Fuel += calculateRequiredFuel1(mass)
        part2Fuel += calculateRequiredFuel2(mass)
    }
    println("===== Part 1 =====")
    println(part1Fuel)
    println("===== Part 2 =====")
    println(part2Fuel)
}
