fun main() {
    fun part1(input: List<String>): Int {
        val monkeyConfigs = mutableListOf<MutableList<String>>()

        input.forEach { line ->
            if (line.startsWith("Monkey")) {
                monkeyConfigs.add(mutableListOf())
            } else if (line.isNotBlank()) {
                monkeyConfigs.last().add(line)
            }
        }

        val monkeys = monkeyConfigs.map { config ->
            Monkey.from(config)
        }

        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.takeTurn(monkeys)
            }
        }

        val monkeyBusiness = monkeys.map { it.inspectionCount }.sortedDescending().let {
            it[0] * it[1]
        }

        return monkeyBusiness
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day11")

    println(part1(input))
    println(part2(input))
}

data class Monkey(
    val items: MutableList<Long>,
    val inspectionOperation: Any,
    val test: Test
    ) {

    var inspectionCount = 0

    private fun inspectItem(idx: Int) {
        inspectionCount++
        increaseWorryValue(idx)
        reduceWorryValue(idx)
    }

    private fun increaseWorryValue(idx: Int) {
        inspectionOperation.also {
            when (it) {
                is Add -> {
                    items[idx] = items[idx] + it.value
                }

                is Multiply -> {
                    items[idx] = items[idx] * it.value
                }

                is Square -> {
                    items[idx] = items[idx] * items[idx]
                }
            }
        }
    }

    private fun reduceWorryValue(idx: Int) {
        items[idx] = items[idx].floorDiv(3)
    }

    private fun testItem(idx: Int): Int {
        return if (items[idx] % test.value == 0L) {
            test.trueMonkey
        } else {
            test.falseMonkey
        }
    }

    private fun throwItem(idx: Int, other: Monkey) {
        other.items.add(items[idx])
    }

    fun takeTurn(monkeys: List<Monkey>) {
        items.forEachIndexed { idx, item ->
            inspectItem(idx)
            val monkeyToThrowTo = testItem(idx)
            throwItem(idx, monkeys[monkeyToThrowTo])
        }
        items.clear()
    }

    companion object {
        fun from(config: List<String>): Monkey {
            val startingItems = config[0].split(":")[1].split(",").map { it.trim().toLong() }.toMutableList()
            val operation = config[1].split("=")[1].let { string ->
                if (string.endsWith("old")) {
                    //squaring
                    Square
                } else if (string.contains("+")) {
                    //adding
                    val value = string.split("+")[1].trim().toInt()
                    Add(value)
                } else {
                    //multiplying
                    val value = string.split("*")[1].trim().toInt()
                    Multiply(value)
                }
            }

            val test = Test()

            config[2].split(" ").last().trim().toInt().also { test.value = it }
            config[3].split(" ").last().trim().toInt().also { test.trueMonkey = it }
            config[4].split(" ").last().trim().toInt().also { test.falseMonkey = it }

            return Monkey(
                startingItems,
                operation,
                test
            )
        }
    }
}

class Add(val value: Int)
class Multiply(val value: Int)
object Square

data class Test(var value: Int = 0, var trueMonkey: Int = 0, var falseMonkey: Int = 0)