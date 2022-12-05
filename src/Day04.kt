fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.map { line -> line.splitAssignmentPair() }

        return pairs.count { (part1, part2) ->
            val sectionList1 = part1.getNumbersInRange()
            val sectionList2 = part2.getNumbersInRange()

            sectionList1.containsAll(sectionList2) || sectionList2.containsAll(sectionList1)
        }
    }

    fun part2(input: List<String>): Int {
        val pairs = input.map { it.splitAssignmentPair() }

        return pairs.count { (part1, part2) ->
            val sectionList1 = part1.getNumbersInRange()
            val sectionList2 = part2.getNumbersInRange()

            sectionList1.any { item -> sectionList2.contains(item) }
                    || sectionList2.any { item -> sectionList1.contains(item) }
        }
    }

    val input = readInput("Day04")

    println(part1(input))
    println(part2(input))
}

fun String.splitAssignmentPair(): List<String> {
    return split(",")
}

fun String.getNumbersInRange(): List<Int> {
    return split("-").map { it.toInt() }.let { (rangeStart, rangeEnd) ->
        (rangeStart..rangeEnd).toList()
    }
}
