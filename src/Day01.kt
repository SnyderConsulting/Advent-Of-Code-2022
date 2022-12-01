fun main() {
    fun getElvesList(input: List<String>): List<List<Int>> {
        val elves = mutableListOf(mutableListOf<Int>())
        var counter = 0

        input.forEach {
            if (it.isBlank()) {
                counter += 1
                elves.add(mutableListOf())
            } else {
                elves[counter].add(it.toInt())
            }
        }

        return elves
    }

    fun part1(input: List<String>): Int {
        val elves = getElvesList(input)

        return elves
            .maxBy { elf ->
                elf.sumOf { calorie -> calorie }
            }.sumOf { calorie -> calorie }
    }

    fun part2(input: List<String>): Int {
        val elves = getElvesList(input)

        return elves
            .sortedByDescending { elf ->
                elf.sumOf { calorie -> calorie }
            }
            .take(3)
            .sumOf { it.sum() }
    }

    val input = readInput("Day01")

    println(part1(input))
    println(part2(input))
}
