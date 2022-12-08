fun main() {
    fun part1(input: List<String>): Int {
        val fileSystem = AOCFileSystem()

        fillFileSystem(input, fileSystem)

        val nestedDirectories = getNestedDirectories(fileSystem)

        return nestedDirectories.map { it.getSize() }.filter { it < 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val fileSystem = AOCFileSystem()

        fillFileSystem(input, fileSystem)

        val nestedDirectories = getNestedDirectories(fileSystem).map { it.getSize() }.sorted()

        val systemSpace = 70 * 1000 * 1000
        val availableSpaceRequired = 30 * 1000 * 1000
        val usedSpace = nestedDirectories.last()
        val availableSpace = systemSpace - usedSpace
        val amountToFree = availableSpaceRequired - availableSpace

        return nestedDirectories.first { it >= amountToFree }
    }

    val input = readInput("Day07")

    println(part1(input))
    println(part2(input))
}

fun getNestedDirectories(fileSystem: AOCFileSystem): List<AOCDirectory> {

    val directories = mutableListOf(fileSystem.root)

    fun checkFolderChildren(directory: AOCDirectory) {
        directories.addAll(directory.subDirectories)
        if (directory.subDirectories.isNotEmpty()) {
            directory.subDirectories.forEach { subDirectory ->
                checkFolderChildren(subDirectory)
            }
        }
    }

    checkFolderChildren(fileSystem.root)

    return directories
}

fun fillFileSystem(input: List<String>, fileSystem: AOCFileSystem) {
    input.forEach {
        when {
            it.contains("$ cd ") -> {
                fileSystem.changeDirectory(it.removePrefix("$ cd "))
            }
            it.contains("$ ") -> {}
            else -> {
                if (it.contains("dir")) {
                    fileSystem.addDirectory(it.removePrefix("dir "))
                } else {
                    val (size, name) = it.split(" ")
                    fileSystem.addFile(AOCFile(name, size.toInt()))
                }
            }
        }
    }
}

class AOCFileSystem {
    val root = AOCDirectory("/", null)
    var currentDirectory = root

    fun addFile(file: AOCFile) {
        currentDirectory.files.add(file)
    }

    fun addDirectory(directoryName: String) {
        currentDirectory.subDirectories.add(AOCDirectory(path = directoryName, parent = currentDirectory))
    }

    fun changeDirectory(path: String) {
        currentDirectory = when (path) {
            ".." -> { currentDirectory.parent!! }
            "/" -> { root }
            else -> { currentDirectory.subDirectories.find { it.path == path }!! }
        }
    }
}

data class AOCDirectory(
    val path: String,
    val parent: AOCDirectory?,
    val files: MutableList<AOCFile> = mutableListOf(),
    val subDirectories: MutableList<AOCDirectory> = mutableListOf()
) {
    private fun getSize(currentDirectory: AOCDirectory): Int {

        val fileSizes = currentDirectory.files.sumOf { it.size }

        if (currentDirectory.subDirectories.isEmpty()) {
            return fileSizes
        }

        return fileSizes + currentDirectory.subDirectories.sumOf {
            getSize(it)
        }
    }

    fun getSize(): Int = getSize(this)
}

data class AOCFile(val name: String, val size: Int)