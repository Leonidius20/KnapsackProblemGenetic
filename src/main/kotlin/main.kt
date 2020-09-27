import kotlin.random.Random
import kotlin.random.nextInt

const val CAPACITY = 250
const val NUMBER_OF_ITEMS = 100

data class Item(val cost: Int, val weight: Int)

val items = Array(NUMBER_OF_ITEMS) {
    Item(Random.nextInt(2, 31), Random.nextInt(1, 26))
}

fun main() {
    val validator = { solution: Array<Boolean> ->
        var sum = 0
        solution.forEachIndexed { index, value ->
            if (value) sum += items[index].weight
        }
        sum <= CAPACITY
    }

    val fitnessFunction = { solution: Array<Boolean> ->
        var sum = 0
        solution.forEachIndexed { index, value ->
            if (value) sum += items[index].cost
        }
        sum
    }

    val crossover = { parent1: Array<Boolean>, parent2: Array<Boolean> ->
        parent1.slice(0 until 25)
                .plus(parent2.slice(25 until 50))
                .plus(parent1.slice(50 until 75))
                .plus(parent2.slice(75 until 100)).toTypedArray()
    }

    val mutation = { solution: Array<Boolean> ->
        if (Random.nextInt(0..100) <= 5) { // 5% probability
            val index1 = Random.nextInt(0 until NUMBER_OF_ITEMS)
            val index2 = Random.nextInt(0 until NUMBER_OF_ITEMS)
            val temp = solution[index1]
            solution[index1] = solution[index2]
            solution[index2] = temp
        }
        solution
    }

    val localImprovement = { solution: Array<Boolean> ->
        solution // TODO
    }

    val result = genetic(solutionLength = NUMBER_OF_ITEMS, validator, fitnessFunction,
            crossover, mutation, localImprovement, maxIterations = 1000)

    println("Cost: ${fitnessFunction(result)}")

    var weight = 0
    result.forEachIndexed { index, value ->
        if (value) weight += items[index].weight
    }
    println("Weight: $weight")
}