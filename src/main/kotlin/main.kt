import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

const val CAPACITY = 250
const val NUMBER_OF_ITEMS = 100

data class Item(val cost: Int, val weight: Int)

val items = Array(NUMBER_OF_ITEMS) {
    Item(cost = Random.nextInt(2, 31),
        weight = Random.nextInt(1, 26))
}

fun main() {
    val validator = { solution: Array<Boolean> ->
        var totalWeight = 0

        solution.forEachIndexed { index, value ->
            if (value) totalWeight += items[index].weight
        }

        while (totalWeight > CAPACITY) {
            val index = solution.randomIndex()

            if (solution[index]) {
                solution[index] = false
                totalWeight -= items[index].weight
            }
        }

        solution
    }

    val fitness = { solution: Array<Boolean> ->
        var sum = 0
        solution.forEachIndexed { index, value ->
            if (value) sum += items[index].cost
        }
        sum
    }

    val crossover = { parent1: Array<Boolean>, parent2: Array<Boolean> ->
        arrayOf(parent1.slice(0 until 25)
                .plus(parent2.slice(25 until 50))
                .plus(parent1.slice(50 until 75))
                .plus(parent2.slice(75 until 100)).toTypedArray(),

                parent2.slice(0 until 25)
                        .plus(parent1.slice(25 until 50))
                        .plus(parent2.slice(50 until 75))
                        .plus(parent1.slice(75 until 100)).toTypedArray())
    }

    val mutation = { solution: Array<Boolean> ->
        if (Random.nextInt(0..100) <= 5) { // 5% probability
            val index1 = solution.randomIndex()
            val index2 = solution.randomIndex()
            solution.swap(index1, index2)
        }
        solution
    }

    val fitnessToWeightRatio = { solution: Array<Boolean> ->
        var fitness = 0.0
        var weight = 0.0
        solution.forEachIndexed { index, value ->
            if (value) {
                weight += items[index].weight
                fitness += items[index].cost
            }
        }
        fitness / weight
    }

    val localImprovement = { sol: Array<Boolean> ->
        var result = sol

        if (Random.nextInt(0..100) <= 5) { // 5% probability

        }

        result
    }

    val file = File("data.csv")
    file.createNewFile()
    val writer = file.printWriter()

    val callback = { iteration: Int, bestCost: Int ->
        writer.println("$iteration,$bestCost")
    }

    val result = GeneticSolver(solutionLength = NUMBER_OF_ITEMS, validator, fitness,
            crossover, mutation, localImprovement, maxIterations = 1000, callback).solve()

    writer.close()

    println("Cost: ${fitness(result)}")

    var weight = 0
    result.forEachIndexed { index, value ->
        if (value) weight += items[index].weight
    }
    println("Weight: $weight")
}