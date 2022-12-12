fun main() {
    fun part1(input: List<String>): Int {
        val grid = mutableListOf<MutableList<Position>>()

        var startPosition = Position(0, 0, 0)
        var endPosition = Position(0, 0, 0)

        input.forEachIndexed { y, line ->
            grid.add(mutableListOf())
            line.forEachIndexed { x, char ->
                if (char == 'S') {
                    //start
                    startPosition = Position(x, y, 0, 0)
                    grid.last().add(startPosition)
                } else if (char == 'E') {
                    //end
                    endPosition = Position(x, y, 25)
                    grid.last().add(endPosition)
                } else {
                    grid.last().add(Position(x, y, ('a'..'z').toList().indexOf(char)))
                }
            }
        }

        val currentPositions = mutableListOf(startPosition)
        val stagedPositions = mutableListOf<Position>()

        while (currentPositions.isNotEmpty()) {
            currentPositions.forEach {
                stagedPositions.addAll(it.getOtherPositions(grid))
            }
            currentPositions.clear()
            currentPositions.addAll(stagedPositions)
            stagedPositions.clear()
        }

        return grid[endPosition.y][endPosition.x].distance!!
    }

    fun part2(input: List<String>): Int {
        val grid = mutableListOf<MutableList<Position>>()

        val startingPositions = mutableListOf<Position>()
        var endPosition = Position(0, 0, 0)

        input.forEachIndexed { y, line ->
            grid.add(mutableListOf())
            line.forEachIndexed { x, char ->
                if (char == 'a' || char == 'S') {
                    //start?
                    val startingPosition = Position(x, y, 0)
                    startingPositions.add(startingPosition)
                    grid.last().add(startingPosition)
                } else if (char == 'E') {
                    //end
                    endPosition = Position(x, y, 25)
                    grid.last().add(endPosition)
                } else {
                    grid.last().add(Position(x, y, ('a'..'z').toList().indexOf(char)))
                }
            }
        }

        val resultList = mutableListOf<Int>()

        startingPositions.forEach { position ->
            position.distance = 0
            val currentPositions = mutableListOf(position)
            val stagedPositions = mutableListOf<Position>()

            while (currentPositions.isNotEmpty()) {
                currentPositions.forEach {
                    stagedPositions.addAll(it.getOtherPositions(grid))
                }
                currentPositions.clear()
                currentPositions.addAll(stagedPositions)
                stagedPositions.clear()
            }

            grid[endPosition.y][endPosition.x].distance?.also {
                resultList.add(it)
            }

            grid.forEach { row ->
                row.forEach {
                    it.distance = null
                }
            }
        }

        return resultList.min()
    }

    val input = readInput("Day12")

    println(part1(input))
    println(part2(input))
}

data class Position(val x: Int, val y: Int, val height: Int, var distance: Int? = null) {

    fun getOtherPositions(grid: List<List<Position>>): MutableList<Position> {
        val otherPositions = mutableListOf<Position>()
        val gridWidth = grid.first().size
        val gridHeight = grid.size

        fun checkPosition(otherPosition: Position) {
            if (otherPosition.height <= height + 1 && otherPosition.distance == null) {
                otherPosition.distance = distance!! + 1
                otherPositions.add(otherPosition)
            }
        }

        if (x != 0) {
            //get left
            checkPosition(grid[y][x - 1])
        }
        if (x != gridWidth - 1) {
            //get right
            checkPosition(grid[y][x + 1])
        }
        if (y != 0) {
            //get bottom
            checkPosition(grid[y - 1][x])
        }
        if (y != gridHeight - 1) {
            //get top
            checkPosition(grid[y + 1][x])
        }

        return otherPositions
    }
}