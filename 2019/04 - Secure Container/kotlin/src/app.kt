import kotlin.math.pow

data class DedupeDigitEntry(val digit: Int, var repeats: Int)

open class NumberValidator(private val number: Int) {
    val encodedNumber = encodeNumber()

    private fun getPowValue(position: Int): Int {
        val base = 10.toDouble()
        val exponent = (5 - position).toDouble()
        return base.pow(exponent).toInt()
    }

    private fun getDigit(position: Int): Int {
        val powValue = getPowValue(position)
        return (number / powValue) % 10
    }

    private fun encodeNumber(): List<DedupeDigitEntry> {
        val firstDigit = getDigit(0)
        val encodedNumber = mutableListOf(DedupeDigitEntry(firstDigit, 1))
        for (i in 1 until 6) {
            val currentDigit = getDigit(i)
            val previousEntry = encodedNumber.last()
            if (currentDigit == previousEntry.digit) {
                previousEntry.repeats += 1
            } else {
                encodedNumber.add(DedupeDigitEntry(currentDigit, 1))
            }
        }
        return encodedNumber.toList()
    }

    private fun isWithinRange(): Boolean {
        return number in 100000..999999
    }

    private fun isIncreasing(): Boolean {
        for (i in 1 until encodedNumber.size) {
            if (encodedNumber[i].digit < encodedNumber[i - 1].digit) {
                return false
            }
        }
        return true
    }

    open fun containsPair(): Boolean {
        for (entry in encodedNumber) {
            if (entry.repeats >= 2) {
                return true
            }
        }
        return false
    }

    fun isValid(): Boolean {
        if (!isWithinRange()) {
            return false
        }
        if (!isIncreasing()) {
            return false
        }
        if (!containsPair()) {
            return false
        }
        return true
    }
}

class NumberValidatorStrict(number: Int): NumberValidator(number) {
    override fun containsPair(): Boolean {
        for (entry in encodedNumber) {
            if (entry.repeats == 2) {
                return true
            }
        }
        return false
    }
}

fun part1(): Int {
    var numberOfValidPasswords = 0
    for (i in 246515..739105) {
        if (NumberValidator(i).isValid()) {
            numberOfValidPasswords += 1
        }
    }
    return numberOfValidPasswords
}

fun part2(): Int {
    var numberOfValidPasswords = 0
    for (i in 246515..739105) {
        if (NumberValidatorStrict(i).isValid()) {
            numberOfValidPasswords += 1
        }
    }
    return numberOfValidPasswords
}

fun main() {
    println("===== Part 1 =====")
    println(part1())
    println("===== Part 2 =====")
    println(part2())
}
