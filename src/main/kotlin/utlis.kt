import kotlin.random.Random
import kotlin.random.nextInt

fun <T, R : Comparable<R>> Array<T>.getTwoMaxBy(selector: (T) -> R): Pair<T, T> {
    var best = this[0]
    var bestFitness = selector(this[0])

    var secondBest = this[0]
    var secondBestFitness = bestFitness

    for (specimen in this) {
        val specimenFitness = selector(specimen)
        if (specimenFitness > bestFitness) {
            secondBest = best
            secondBestFitness = bestFitness

            best = specimen
            bestFitness = specimenFitness
        } else if (specimenFitness > secondBestFitness) {
            secondBest = specimen
            secondBestFitness = specimenFitness
        }
    }

    return Pair(best, secondBest)
}

fun <T> ArrayList<T>.add(pair: Pair<T, T>) {
    this.addAll(pair.toList())
}

fun <T> Array<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

fun <T> Array<T>.randomIndex(): Int {
    return Random.nextInt(this.indices)
}