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

    var iterationCounter = 0
    while (iterationCounter <= maxIterations) {
        iterationCounter++

        // finding best and worst solutions
        val (bestIndex, worstIndex)
                = evaluateSolutions(population, fitness)

        // selecting parents for a new solution
        val parent1 = population[bestIndex]

        var parent2 = population.random()
        while (parent2.contentEquals(parent1)) {
            parent2 = population.random() // potential endless loop?
        }

        // creating a new solution
        val newSolution = localImprovement(mutation(crossover(parent1, parent2)))

        // validating a new solution
        if (!validator(newSolution)) continue

        // replacing the worst solution with the new one
        population[worstIndex] = newSolution
    }

    return population[evaluateSolutions(population, fitness).bestIndex]
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