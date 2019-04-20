# INFO6205_523
## Genetic Algorithm implementation
#### Problem Statement
***World cup cricket match scheduling***. Create a schedule for matches between two teams which satisfies below criteria.
* No two matches should happen on the same day
* No team should play on two consecutive days
* Each team should have played exactly 2 matches with every other teams
* Each team should have played exactly one match on their home ground with each opponent
* Try to avoid scheduling a match on a day with bad weather    
#### Algorithm Correlation
For given Problem we have considered one single match as "gene". The collection of Matches to 
form a whole match schedule is considered as Chromosome. Different random schedules create population.

Fitness score is calculated based on the number of constraints violated.
#### Implementation
* Implemented multi-threading for running the algorithm on colonies grouped from the total population (GeneticAlgorithmDriver.java)
* Using properties file for configuring parameters for the Genetic Algorithm
* Using log4j to log the outputs from each generation
#### Run Instruction
