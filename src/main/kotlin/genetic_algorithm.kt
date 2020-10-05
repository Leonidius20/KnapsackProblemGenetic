import kotlin.random.Random
import kotlin.random.nextInt

private typealias Solution = Array<Boolean>

class GeneticSolver(private val solutionLength: Int,
                    private val validator: (Solution) -> Solution,
                    private val fitness: (Solution) -> Int,
                    private val crossover: (Solution, Solution) -> Array<Solution>,
                    private val mutation: (Solution) -> Solution,
                    private val localImprovement: (Solution) -> Solution,
                    private val maxIterations: Int,
                    private val callback: (Int, Int) -> Unit) {


    private lateinit var population : Array<Solution>


    fun solve(): Solution {
        population = getFirstGeneration()

        for (iteration in 0 until maxIterations) {
            population = getNextGeneration()

            callback(iteration, population.maxOf(fitness))
        }

        return population.maxByOrNull(fitness)!!
    }

    /**
     * Generates solutions, each of which has only one boolean set to 1
     * number of solutions in this population == solution length
     */
    private fun getFirstGeneration(): Array<Solution> {
        var index = -1
        return Array(solutionLength) {
            index++
            Solution(solutionLength) { it == index }
        }
    }

    private fun getNextGeneration(): Array<Solution> {
        val nextGen = ArrayList<Solution>(population.size)

        nextGen.add(population.getTwoMaxBy(fitness)) // elitism

        val fitnessSum = population.sumBy(fitness) // needed for parent selection

        for (i in 2 until population.size step 2) {
            val (offspring1, offspring2) = crossover(
                    selectParent(fitnessSum), selectParent(fitnessSum))

            nextGen.add(validator(localImprovement(mutation(offspring1))))
            nextGen.add(validator(localImprovement(mutation(offspring2))))
        }

        return nextGen.toTypedArray()
    }

    private fun selectParent(fitnessSum: Int): Solution { // Roulette-wheel selection
        val threshold = Random.nextInt(0..fitnessSum)

        var partialSum = 0

        for (solution in population) {
            partialSum += fitness(solution)
            if (partialSum >= threshold) {
                return solution
            }
        }

        return population[0] // unreachable
    }

}