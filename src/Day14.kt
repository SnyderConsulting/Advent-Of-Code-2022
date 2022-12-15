fun main() {
    fun part1(input: List<String>): Int {
        val rockLines = input.map { line ->
            line.split(" -> ").map {
                Coordinate.from(it)
            }.fillRockLines()
        }.flatten()

        val (left, right, bottom) = rockLines.getEdgeLocations()

        val grid = List(bottom + 1) { MutableList(right - left + 1) { false } }

        rockLines.forEach { rock ->
            grid[rock.y][rock.x - left] = true
        }

        grid.forEach { line ->
            println(line.map { isFilled ->
                if (isFilled) "#" else "."
            })
        }

        var counter = 0
        var sandStopped = true

        while (sandStopped) {
            sandStopped = simulateSandP1(grid, 500 - left, bottom, right - left)
            if (sandStopped) {
                counter++
            }
        }

        return counter
    }

    fun part2(input: List<String>): Int {
        val rockLines = input.map { line ->
            line.split(" -> ").map {
                Coordinate.from(it)
            }.fillRockLines()
        }.flatten().toMutableList()

        val (_, _, bottom) = rockLines.getEdgeLocations()

        var counter = 0
        var sandStopped = true

        while (sandStopped) {
            counter++
            sandStopped = simulateSandP2(filledLocations = rockLines, bottom + 2)

            println(rockLines.size)
        }

        return counter
    }

    val input = readInput("Day14")

    println(part1(input))
    println(part2(input))
}

fun simulateSandP1(grid: List<MutableList<Boolean>>, startingX: Int, rockBottom: Int, rockRight: Int): Boolean {
    val sandPosition = Coordinate(startingX, 0)

    while (true) {
        if (sandPosition.y == rockBottom) {
            return false
        }

        if (!grid[sandPosition.y + 1][sandPosition.x]) {
            //can move down
            sandPosition.y += 1
        } else if (sandPosition.x == 0) {
            return false
        } else if (!grid[sandPosition.y + 1][sandPosition.x - 1]) {
            //can move left
            sandPosition.y += 1
            sandPosition.x -= 1
        } else if (sandPosition.x == rockRight) {
            return false
        } else if (!grid[sandPosition.y + 1][sandPosition.x + 1]) {
            //can move right
            sandPosition.y += 1
            sandPosition.x += 1
        } else {
            grid[sandPosition.y][sandPosition.x] = true
            return true
        }
    }
}

fun simulateSandP2(filledLocations: MutableList<Coordinate>, rockBottom: Int): Boolean {
    val sandPosition = Coordinate(500, 0)

    while (true) {
        if (sandPosition.y + 1 == rockBottom) {
            filledLocations.add(sandPosition)
            return true
        } else if (!filledLocations.contains(Coordinate(sandPosition.x, sandPosition.y + 1))) {
            //can move down
            sandPosition.y += 1
        } else if (!filledLocations.contains(Coordinate(sandPosition.x - 1, sandPosition.y + 1))) {
            //can move left
            sandPosition.y += 1
            sandPosition.x -= 1
        } else if (!filledLocations.contains(Coordinate(sandPosition.x + 1, sandPosition.y + 1))) {
            //can move right
            sandPosition.y += 1
            sandPosition.x += 1
        } else {
            return if (sandPosition.x == 500 && sandPosition.y == 0) {
                false
            } else {
                filledLocations.add(sandPosition)
                true
            }
        }
    }
}

data class Coordinate(var x: Int, var y: Int) {
    companion object {
        fun from(string: String): Coordinate {
            return Coordinate(
                x = string.split(",")[0].toInt(),
                y = string.split(",")[1].toInt(),
            )
        }
    }
}

fun List<Coordinate>.fillRockLines(): List<Coordinate> {
    val rockLines = mutableListOf<Coordinate>()

    windowed(2).forEach { (start, end) ->
        rockLines.add(start)
        rockLines.add(end)
        if (start.x == end.x) {
            //moving vertically

            if (start.y > end.y) {
                //moving up
                repeat(start.y - end.y) {
                    rockLines.add(
                        Coordinate(start.x, start.y - it)
                    )
                }
            } else {
                //moving down
                repeat(end.y - start.y) {
                    rockLines.add(
                        Coordinate(start.x, start.y + it)
                    )
                }
            }
        } else if (start.y == end.y) {
            //moving horizontally

            if (start.x > end.x) {
                //moving left
                repeat(start.x - end.x) {
                    rockLines.add(
                        Coordinate(start.x - it, start.y)
                    )
                }
            } else {
                //moving right
                repeat(end.x - start.x) {
                    rockLines.add(
                        Coordinate(start.x + it, start.y)
                    )
                }
            }
        }
    }

    return rockLines.distinct()
}

//Left, Right, Bottom
fun List<Coordinate>.getEdgeLocations(): List<Int> {
    val left = minBy { it.x }.x
    val right = maxBy { it.x }.x
    val bottom = maxBy { it.y }.y

    return listOf(left, right, bottom)
}