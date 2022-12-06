fun main() {
    fun part1(input: List<String>): Int {

        val packetLength = 4

        return getEndOfSubset(input[0], packetLength)
    }

    fun part2(input: List<String>): Int {

        val messageLength = 14

        return getEndOfSubset(input[0], messageLength)
    }

    val input = readInput("Day06")

    println(part1(input))
    println(part2(input))
}

fun getEndOfSubset(string: String, sectionLength: Int): Int {
    return string.windowed(sectionLength).map {
        it.toList().distinct().size
    }.indexOfFirst {
        it == sectionLength
    } + sectionLength
}