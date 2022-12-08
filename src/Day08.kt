fun main() {
    fun part1(input: List<String>): Int {
        val treeGrid = fillTreeGrid(input)

        checkRtl(input, treeGrid)
        checkLtr(input, treeGrid)
        checkTtb(input, treeGrid)
        checkBtt(input, treeGrid)

        return treeGrid.flatten().count { it }
    }

    fun part2(input: List<String>): Int {
        val treeGrid = fillTreeGridForViewing(input)

        checkRtlForViewing(input, treeGrid)
        checkLtrForViewing(input, treeGrid)
        checkTtbForViewing(input, treeGrid)
        checkBttForViewing(input, treeGrid)

        return treeGrid.flatten().maxOf { it.getViewingScore() }
    }

    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}

//region part1
fun fillTreeGrid(input: List<String>): List<MutableList<Boolean>> {
    val treeGrid = mutableListOf<MutableList<Boolean>>()
    input.forEach { line ->
        treeGrid.add(mutableListOf())
        repeat(line.length) {
            treeGrid.last().add(false)
        }
    }

    return treeGrid
}

fun checkLtr(input: List<String>, treeGrid: List<MutableList<Boolean>>) {
    input.forEachIndexed { y, line ->
        var tallestTree = -1

        line.forEachIndexed { x, char ->
            if (char.toString().toInt() > tallestTree) {
                tallestTree = char.toString().toInt()
                treeGrid[y][x] = true
            }
        }
    }
}

fun checkRtl(input: List<String>, treeGrid: List<MutableList<Boolean>>) {
    input.forEachIndexed { y, line ->
        var tallestTree = -1

        line.reversed().forEachIndexed { x, char ->
            if (char.toString().toInt() > tallestTree) {
                tallestTree = char.toString().toInt()
                treeGrid[y][line.length - 1 - x] = true
            }
        }
    }
}

fun checkTtb(input: List<String>, treeGrid: List<MutableList<Boolean>>) {
    repeat(input.first().length) { x ->
        var tallestTree = -1

        input.forEachIndexed { y, line ->
            if (line[x].toString().toInt() > tallestTree) {
                tallestTree = line[x].toString().toInt()
                treeGrid[y][x] = true
            }
        }
    }
}

fun checkBtt(input: List<String>, treeGrid: List<MutableList<Boolean>>) {
    repeat(input.first().length) { x ->
        var tallestTree = -1

        input.reversed().forEachIndexed { y, line ->
            if (line[x].toString().toInt() > tallestTree) {
                tallestTree = line[x].toString().toInt()
                treeGrid[input.size - 1 - y][x] = true
            }
        }
    }
}
//endregion

//region part2
fun fillTreeGridForViewing(input: List<String>): List<MutableList<ViewingDistance>> {
    val treeGrid = mutableListOf<MutableList<ViewingDistance>>()
    input.forEach { line ->
        treeGrid.add(mutableListOf())
        repeat(line.length) {
            treeGrid.last().add(ViewingDistance())
        }
    }

    return treeGrid
}

fun checkLtrForViewing(input: List<String>, treeGrid: List<MutableList<ViewingDistance>>) {
    input.forEachIndexed { y, line ->
        val trees = mutableListOf<Int>()

        line.forEachIndexed { x, char ->
            val height = char.toString().toInt()
            treeGrid[y][x].apply {
                this.left = trees.toList().reversed()
                this.height = height
            }

            trees.add(height)
        }
    }
}

fun checkRtlForViewing(input: List<String>, treeGrid: List<MutableList<ViewingDistance>>) {
    input.forEachIndexed { y, line ->
        val trees = mutableListOf<Int>()

        line.reversed().forEachIndexed { x, char ->
            val height = char.toString().toInt()
            treeGrid[y][line.length - 1 - x].apply {
                this.right = trees.toList().reversed()
                this.height = height
            }

            trees.add(height)
        }
    }
}

fun checkTtbForViewing(input: List<String>, treeGrid: List<MutableList<ViewingDistance>>) {
    repeat(input.first().length) { x ->
        val trees = mutableListOf<Int>()

        input.forEachIndexed { y, line ->
            val height = line[x].toString().toInt()
            treeGrid[y][x].apply {
                this.top = trees.toList().reversed()
                this.height = height
            }

            trees.add(height)
        }
    }
}

fun checkBttForViewing(input: List<String>, treeGrid: List<MutableList<ViewingDistance>>) {
    repeat(input.first().length) { x ->
        val trees = mutableListOf<Int>()

        input.reversed().forEachIndexed { y, line ->
            val height = line[x].toString().toInt()
            treeGrid[input.size - 1 - y][x].apply {
                this.bottom = trees.toList().reversed()
                this.height = height
            }

            trees.add(height)
        }
    }
}

data class ViewingDistance(
    var height: Int = 0,
    var top: List<Int> = emptyList(),
    var bottom: List<Int> = emptyList(),
    var left: List<Int> = emptyList(),
    var right: List<Int> = emptyList()
) {

    fun getViewingScore(): Int {
        val topVisible = top.getViewingScore(height)
        val bottomVisible = bottom.getViewingScore(height)
        val leftVisible = left.getViewingScore(height)
        val rightVisible = right.getViewingScore(height)

        return topVisible * bottomVisible * leftVisible * rightVisible
    }
}

fun List<Int>.getViewingScore(height: Int): Int {
    return indexOfFirst { it >= height }.let {
        if (it == -1) {
            this
        } else {
            toMutableList().dropLast(size - it - 1)
        }
    }.size
}
//endregion