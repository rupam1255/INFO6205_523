package scheduling;

import scheduling.pojo.Match;
import scheduling.pojo.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class executes various process during evolution such as evolve, crossover and mutation
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */

class Algorithm {

    /**
     * A constant variable that defines the rate at which the crossover happens
     */
    private static final double CROSSOVER_RATE = Driver.CROSSOVER_RATE;

    /**
     * A constant variable that defines the probability of Mutation
     */
    private static final double MUTATION_RATE = Driver.MUTATION_RATE;

    /**
     * A constant variable that defines the size of the Tournament
     */
    private static final int TOURNAMENT_SIZE = Driver.TOURNAMENT_SELECTION_SIZE;

    /**
     * A variable to hold the data set
     */
    private Data data;

    /**
     * Parameterized constructor for creating an instance for the scheduling.Algorithm class
     *
     * @param data The data set over which the algorithm is going to be run
     */
    Algorithm(Data data) {
        this.data = data;
        this.data.initializeData();
    }

    /**
     * This function performs the evolution on a selected population
     *
     * @param pop The population over which evolution has to be done
     * @return The evolved population
     */
    Population evolve(Population pop) {
        pop.sortByFitness();
        Population newPopulation = new Population(pop.size(), data, false);
        while (newPopulation.size() < pop.size()) {
            Schedule s1 = parentSelection(pop);
            Schedule s2 = parentSelection(pop);
//            Schedule newSchedule = crossover(s1, s2);
//            newPopulation.addSchedule(newSchedule);
            List<Schedule> children = crossover(s1, s2);
            newPopulation.getSchedules().addAll(children);
        }
        for (Schedule s : newPopulation.getSchedules()) {
            mutation(s);
            s.sortMatchesByDate();
        }
        return newPopulation;
    }

    /**
     * This function performs crossover operation. In this case, we have implemented "Sexual Reproduction".
     *
     * @param s1 The first parent solution set
     * @param s2 The second parent solution set
     * @return The next generation solution set
     */
    private List<Schedule> crossover(Schedule s1, Schedule s2) {
        Schedule newSol = new Schedule(data);
        Schedule newSol2 = new Schedule(data);
        // Loop through genes
        int crossoverPoint = (int) (Math.random() * s1.size());
        for (int i = 0; i < s1.size(); i++) {
            if (i <= crossoverPoint) {
                newSol.getMatchList().set(i, s2.getMatchList().get(i));
                newSol2.getMatchList().set(i, s1.getMatchList().get(i));
            } else {
                newSol.getMatchList().set(i, s1.getMatchList().get(i));
                newSol2.getMatchList().set(i, s2.getMatchList().get(i));
            }
        }
        return new ArrayList<>(Arrays.asList(newSol, newSol2));
    }

    /**
     * This function mutates the chromosome (A single solution set)
     *
     * @param s1 The solution set that has to be mutated
     */
    private void mutation(Schedule s1) {
        Schedule tempSchedule = new Schedule(data);
        for (int i = 0; i < s1.size(); i++) {
            if (Math.random() <= MUTATION_RATE) {
                // Create random gene
                Match fix = tempSchedule.getMatchList().get(i);
                s1.getMatchList().set(i, fix);
            }
        }
    }

    /**
     * This function selects the population which are fit for the next iteration
     *
     * @param pop The population in which the good fits has to be found
     * @return The fittest solution
     */
    private Schedule parentSelection(Population pop) {
        Population tournament = new Population(TOURNAMENT_SIZE, data, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomId = (int) (Math.random() * pop.size() * Driver.CULLING_RATE);
            tournament.getSchedules().add(pop.getSchedules().get(randomId));
        }
        // Get the fittest
        return tournament.getFittest();
    }
}
