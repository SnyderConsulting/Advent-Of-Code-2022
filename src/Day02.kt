sealed class Choice {
    abstract val points: Int

    companion object {
        fun parseChoice(value: String): Choice {
            return when (value) {
                "A", "X" -> Rock
                "B", "Y" -> Paper
                "C", "Z" -> Scissors
                else -> Rock
            }
        }

        fun calculateChoiceForResult(opponent: Choice, desiredResult: Result): Choice {
            return when(desiredResult) {
                Win -> {
                    when(opponent) {
                        Paper -> Scissors
                        Rock -> Paper
                        Scissors -> Rock
                    }
                }
                Loss -> {
                    when(opponent) {
                        Paper -> Rock
                        Rock -> Scissors
                        Scissors -> Paper
                    }
                }
                Draw -> opponent
            }
        }
    }
}

object Rock : Choice() {
    override val points: Int
        get() = 1
}

object Paper : Choice() {
    override val points: Int
        get() = 2
}

object Scissors : Choice() {
    override val points: Int
        get() = 3
}

sealed class Result {
    abstract val points: Int
}

object Loss : Result() {
    override val points: Int
        get() = 0
}

object Draw : Result() {
    override val points: Int
        get() = 3
}

object Win : Result() {
    override val points: Int
        get() = 6
}

data class Round(val opponent: Choice, val instruction: Choice) {
    fun getResult(): Result {
        return when {
            opponent == instruction -> Draw
            opponent == Rock && instruction == Scissors -> Loss
            opponent == Paper && instruction == Rock -> Loss
            opponent == Scissors && instruction == Paper -> Loss
            else -> Win
        }
    }

    companion object {
        fun parseResult(value: String): Result {
            return when(value) {
                "X" -> Loss
                "Y" -> Draw
                "Z" -> Win
                else -> Loss
            }
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        /**
         * A = Rock
         * B = Paper
         * C = Scissors
         * X = Rock (1)
         * Y = Paper (2)
         * Z = Scissors (3)
         * Loss = 0
         * Draw = 3
         * Win = 6
         */

        val rounds = input.map { line ->
            line.split(" ").let { (opponent, instruction) ->
                Round(
                    opponent = Choice.parseChoice(opponent),
                    instruction = Choice.parseChoice(instruction)
                )
            }
        }

        return rounds.sumOf { round ->
            round.getResult().points + round.instruction.points
        }
    }

    fun part2(input: List<String>): Int {
        /**
         * A = Rock
         * B = Paper
         * C = Scissors
         * X = Loss
         * Y = Draw
         * Z = Win
         * Loss = 0
         * Draw = 3
         * Win = 6
         */

        val rounds = input.map { line ->
            line.split(" ").let { (opponent, desiredResult) ->
                Round(
                    Choice.parseChoice(opponent),
                    Choice.calculateChoiceForResult(
                        opponent = Choice.parseChoice(opponent),
                        desiredResult = Round.parseResult(desiredResult)
                    )
                )
            }
        }

        return rounds.sumOf { round ->
            round.getResult().points + round.instruction.points
        }
    }

    val input = readInput("Day02")

    println(part1(input))
    println(part2(input))
}
