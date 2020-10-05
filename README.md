# KnapsackProblemGenetic
This program solves the 0-1 knapsack problem using the genetic algorithm. The capacity of the knapsack is set to 250. The program generates 100 items with random costs from 2 to 30 and random weights from 1 to 25. The list of generated items is output to `items.csv` as a list of `cost` - `weight` pairs. The list of items packed to the racksack is printed to the console. Every 20 iterations a `number of iteration` - `highest total cost from the current population` pair is printed to `data.csv`.  

## Initial population
The size of the population is equal to the number of available items (100). The initial population contains solutions, each of which includes only one (unique) item into the racksack.

## Parent selection
Parents are selected using roulette-wheel selection (i.e. the higher a solution's fitness is, the higher chance it has to be selected as a parent).

## Operators
### Crossover
The crossover operator divides parents using 3 points into 4 equal parts and combines these parts alternately.

### Mutation
Two random values in a solution are swapped with the probability of 5%.

### Local improvement
Tries to add one element to the racksack, the cost of which is bigger than 15 and the weight is smaller than 12.

### Validator
Makes sure that the total weight for a solution doesn't exceed the racksack's capacity. If it does, the validator takes random items out of the racksack until the total weight doesn't exceed the capacity.
