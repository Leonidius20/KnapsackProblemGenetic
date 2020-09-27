import kotlin.random.Random
import kotlin.random.nextInt

private typealias Solution = Array<Boolean>
private typealias Validator = (Solution) -> Boolean

fun genetic(solutionLength: Int,
            validator: Validator,
            fitness: (Solution) -> Int,
            crossover: (Solution, Solution) -> Solution,
            mutation: (Solution) -> Solution,
            localImprovement: (Solution) -> Solution,
            maxIterations: Int): Solution {

    val population = generateRandomSolutions(solutionLength)
    population.sortByDescending(fitness)

    var iterationCounter = 0
    while (iterationCounter <= maxIterations) {
        iterationCounter++

        // taking the best half of the population to select parents from
        val bestSolutions = population.sliceArray(0 until population.size / 2)

        // new solutions take places of the worst half of the population
        for (index in population.size / 2 until population.size) {
            val (parent1, parent2) = selectParents(bestSolutions, fitness)

            val newSolution = localImprovement(mutation(crossover(parent1, parent2)))

            if (!validator(newSolution)) continue

            population[index] = newSolution
        }

        population.sortByDescending(fitness)
    }

    return population[0]
}

/**
 * Generates random solutions, each of which has only one boolean set to 1
 * solution length == number of solutions in this population
 */
private fun generateRandomSolutions(solutionLength: Int): Array<Solution> {
    var index = -1
    return Array(solutionLength) {
        index++
        Solution(solutionLength) { it == index }
    }
}

private data class BestAndWorstSolutions(val bestIndex: Int, val worstIndex: Int)

private fun evaluateSolutions(population: Array<Solution>, fitness: (Solution) -> Int): BestAndWorstSolutions {
    var worstIndex = 0
    var worstFitness: Int = fitness(population[0])

    var bestIndex = 0
    var bestFitness: Int = fitness(population[0])

    population.forEachIndexed { index, solution ->
        val solutionFitness = fitness(solution)

        if (solutionFitness < worstFitness) {
            worstIndex = index
            worstFitness = solutionFitness
        }

        if (solutionFitness > bestFitness) {
            bestIndex = index
            bestFitness = solutionFitness
        }
    }

    return BestAndWorstSolutions(bestIndex, worstIndex)
}

data class Parents(val parent1: Solution, val parent2: Solution)

private fun selectParents(population: Array<Solution>, fitness: (Solution) -> Int): Parents {
    val fitnessSum = population.sumBy(fitness)

    fun selectParent(): Solution {
        val threshold = Random.nextInt(0..fitnessSum)
        var partialSum = 0

        var result = population[0]
        for (solution in population) {
            partialSum += fitness(solution)
            if (partialSum >= threshold) {
                result = solution
                break
            }
        }

        return result
    }

    return Parents(selectParent(), selectParent())
}