fun main() {
    fun part1(input: List<String>): Int {
        val rucksacks = input.map { line ->
            Pair(
                line.take(line.length / 2),
                line.drop(line.length / 2)
            )
        }

        return rucksacks.sumOf { rucksack ->
            rucksack.getDuplicate().getPointValue()
        }
    }

    fun part2(input: List<String>): Int {
        val elfList1 = mutableListOf<String>()
        val elfList2 = mutableListOf<String>()
        val elfList3 = mutableListOf<String>()

        var counter = 0

        input.forEach { rucksack ->
            when (counter) {
                0 -> {
                    elfList1.add(rucksack)
                    counter++
                }
                1 -> {
                    elfList2.add(rucksack)
                    counter++
                }
                2 -> {
                    elfList3.add(rucksack)
                    counter = 0
                }
            }
        }

        val groups = mutableListOf<Triple<String, String, String>>()

        repeat(input.size / 3) { idx ->
            groups.add(
                Triple(
                    elfList1[idx],
                    elfList2[idx],
                    elfList3[idx]
                )
            )
        }

        return groups.sumOf { group ->
            group.getDuplicate().getPointValue()
        }
    }

    val input = readInput("Day03")

    println(part1(input))
    println(part2(input))
}

fun Pair<String, String>.getDuplicate(): Char {
    val compartment1 = first.toList().distinct()
    val compartment2 = second.toList().distinct()

    return compartment1.first { char ->
        compartment2.contains(char)
    }
}

fun Triple<String, String, String>.getDuplicate(): Char {
    val rucksack1 = first.toList().distinct()
    val rucksack2 = second.toList().distinct()
    val rucksack3 = third.toList().distinct()

    return rucksack1.first { char ->
        rucksack2.contains(char) && rucksack3.contains(char)
    }
}

fun Char.getPointValue(): Int {
    val charList = listOf('a'..'z', 'A'..'Z').flatten()

    return charList.indexOf(this) + 1
}