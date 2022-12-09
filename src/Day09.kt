import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val movements = input.map { line ->
            Movement.of(line)
        }

        val grid = Grid(2)

        movements.forEach {
            grid.moveHead(it)
        }

        return grid.knotPositions.last().distinct().size
    }

    fun part2(input: List<String>): Int {
        val movements = input.map { line ->
            Movement.of(line)
        }

        val grid = Grid(10)

        movements.forEach {
            grid.moveHead(it)
        }

        return grid.knotPositions.last().distinct().size
    }

    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}

data class Movement(val direction: Direction, val amt: Int) {

    companion object {
        fun of(string: String): Movement {
            val (directionCode, amt) = string.split(" ")

            val direction = when (directionCode) {
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                else -> Direction.RIGHT
            }

            return Movement(direction, amt.toInt())
        }
    }
}

class Grid(knotCount: Int) {
    val knotPositions = MutableList(knotCount) {
        mutableListOf(Coordinates(0, 0))
    }

    fun moveHead(movement: Movement) {
        repeat(movement.amt) {
            val headPositions = knotPositions.first()

            when (movement.direction) {
                Direction.UP -> {
                    headPositions.last().also { currentPos ->
                        headPositions.add(Coordinates(currentPos.x, currentPos.y + 1))
                    }
                }

                Direction.DOWN -> {
                    headPositions.last().also { currentPos ->
                        headPositions.add(Coordinates(currentPos.x, currentPos.y - 1))
                    }
                }

                Direction.LEFT -> {
                    headPositions.last().also { currentPos ->
                        headPositions.add(Coordinates(currentPos.x - 1, currentPos.y))
                    }
                }

                Direction.RIGHT -> {
                    headPositions.last().also { currentPos ->
                        headPositions.add(Coordinates(currentPos.x + 1, currentPos.y))
                    }
                }
            }

            knotPositions.windowed(2).forEach { (headPositions, tailPositions) ->
                moveTail(headPositions.last(), tailPositions)
            }
        }
    }

    private fun moveTail(headPos: Coordinates, tailPositions: MutableList<Coordinates>) {
        var tailPos = tailPositions.last()

        fun moveVertical() {
            //vertical move
            if (headPos.y > tailPos.y) {
                tailPositions.add(Coordinates(tailPos.x, headPos.y - 1))
            } else {
                tailPositions.add(Coordinates(tailPos.x, headPos.y + 1))
            }
        }

        fun moveHorizontal() {
            //horizontal move
            if (headPos.x > tailPos.x) {
                tailPositions.add(Coordinates(headPos.x - 1, tailPos.y))
            } else {
                tailPositions.add(Coordinates(headPos.x + 1, tailPos.y))
            }
        }

        fun moveDiagonal() {
            var yOffset = 0
            var xOffset = 0
            if (headPos.x > tailPos.x) {
                //moving right
                xOffset = 1
            }
            if (headPos.x < tailPos.x) {
                //moving left
                xOffset = -1
            }
            if (headPos.y > tailPos.y) {
                //moving up
                yOffset = 1
            }
            if (headPos.y < tailPos.y) {
                //moving down
                yOffset = -1
            }
            tailPositions.add(Coordinates(tailPos.x + xOffset, tailPos.y + yOffset))
        }

        if (abs(headPos.x - tailPos.x) <= 1 && abs(headPos.y - tailPos.y) <= 1) {
            tailPositions.add(tailPos)
        } else {
            if (headPos.x == tailPos.x) {
                moveVertical()
            } else if (headPos.y == tailPos.y) {
                moveHorizontal()
            } else {
                //diagonal move
                val xDiff = abs(headPos.x - tailPos.x)
                val yDiff = abs(headPos.y - tailPos.y)

                if (xDiff > yDiff) {
                    //move left or right of head
                    tailPos = tailPos.copy(y = headPos.y)
                    moveHorizontal()
                } else if (yDiff > xDiff) {
                    //move up or down of head
                    tailPos = tailPos.copy(x = headPos.x)
                    moveVertical()
                } else {
                    moveDiagonal()
                }
            }
        }
    }
}

data class Coordinates(val x: Int, val y: Int)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}