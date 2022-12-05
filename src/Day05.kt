import java.util.*

fun main() {
    fun part1(input: List<String>): String {
        val (crateRows, instructions) = input.separateFileSections()

        val stackList = mutableListOf<Stack<String>>()

        getCrateList(crateRows).forEach { crateList ->
            crateList.forEachIndexed { index, string ->
                if (stackList.size <= index) {
                    stackList.add(Stack())
                }

                if (string.isNotBlank()) {
                    stackList[index].push(string)
                }
            }
        }

        getInstructions(instructions).forEach { instruction ->
            val toStack = stackList[instruction.to]
            val fromStack = stackList[instruction.from]

            repeat(instruction.qty) {
                toStack.push(fromStack.pop())
            }
        }

        return getTopCrates(stackList.toMutableList())
    }

    fun part2(input: List<String>): String {
        val (crateRows, instructions) = input.separateFileSections()

        val stackList = mutableListOf<MutableList<String>>()

        getCrateList(crateRows).forEach {
            it.forEachIndexed { index, string ->
                if (stackList.size <= index) {
                    stackList.add(mutableListOf())
                }

                if (string.isNotBlank()) {
                    stackList[index].add(string)
                }
            }
        }

        getInstructions(instructions).forEach { instruction ->
            val toStack = stackList[instruction.to]
            val fromStack = stackList[instruction.from]

            toStack.addAll(fromStack.takeLast(instruction.qty))
            repeat(instruction.qty) {
                fromStack.removeLast()
            }
        }

        return getTopCrates(stackList)
    }

    val input = readInput("Day05")

    println(part1(input))
    println(part2(input))
}

fun getCrateList(crateRows: List<String>): List<List<String>> {
    return crateRows.map { row ->
        row.chunked(4).map { crate ->
            crate.replace("[", "").replace("]", "").trim()
        }
    }.reversed()
}

fun getInstructions(instructions: List<String>): List<Instruction> {
    return instructions.map {
        val splitString = it.split(" ")

        val qty = splitString[1].toInt()
        val from = splitString[3].toInt() - 1
        val to = splitString[5].toInt() - 1

        Instruction(qty, from, to)
    }
}

fun List<String>.separateFileSections(): Pair<List<String>, List<String>> {
    val group1End = indexOfFirst { !it.contains("[") }
    val group2Start = indexOfFirst { it.contains("move") }

    return Pair(
        subList(0, group1End),
        subList(group2Start, size)
    )
}

fun getTopCrates(stackList: MutableList<MutableList<String>>): String {
    return stackList.joinToString("") { it.last() }
}

data class Instruction(val qty: Int, val from: Int, val to: Int)