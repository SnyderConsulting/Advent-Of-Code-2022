import kotlin.properties.Delegates

fun main() {
    fun part1(input: List<String>): Int {

        val cpu = CPU()

        input.forEach { cpu.update(it) }

        return cpu.signalStrengths.sum()
    }

    fun part2(input: List<String>) {
        val cpu = CPU()

        input.forEach { cpu.update(it) }

        cpu.pixelGrid.forEach {
            println(it.joinToString(""))
        }
    }

    val input = readInput("Day10")

    println(part1(input))
    part2(input)
}

class CPU {
    private var currentCycle: Int by Delegates.observable(1) { property, oldValue, newValue ->
        checkForSignalStrength()
    }

    private var registerX = 1
    val signalStrengths = mutableListOf<Int>()

    val pixelGrid = mutableListOf(mutableListOf<String>())

    fun update(it: String) {
        if (it == "noop") {
            noop()
        } else {
            val (_, value) = it.split(" ")
            addx(value.toInt())
        }
    }

    private fun Int.getSpriteRange(): List<Int> {
        return listOf(this - 1, this, this + 1)
    }

    private fun drawToCRT() {
        val pixelIndex = pixelGrid.last().size

        pixelGrid.last().add(
            if (registerX.getSpriteRange().contains(pixelIndex)) "#" else "."
        )

        if (pixelIndex >= 39) {
            pixelGrid.add(mutableListOf())
        }
    }

    private fun noop() {
        drawToCRT()
        currentCycle++
    }

    private fun addx(value: Int) {
        drawToCRT()
        currentCycle++
        drawToCRT()
        registerX += value
        currentCycle++
    }

    private fun checkForSignalStrength() {
        if (isAtInterval()) {
            signalStrengths.add(currentCycle * registerX)
        }
    }

    private fun isAtInterval(): Boolean {
        return (currentCycle + 20) % 40 == 0
    }
}