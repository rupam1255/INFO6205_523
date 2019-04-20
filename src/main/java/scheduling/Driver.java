

package scheduling;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import scheduling.pojo.Match;
import scheduling.pojo.Schedule;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Driver to start execution of algorithm
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */

public class Driver {
    /**
     * Path of the properties file
     */
    private static final String PROPERTIES_FILE_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scheduling.properties").toString();
    
    /**
     * Logger object
     */
    private static final Logger log = Logger.getLogger(Driver.class);
    
    /**
     * Genetic algorithm properties
     */
    static double MUTATION_RATE = 0.05;
    static double CROSSOVER_RATE = 0.5;
    static int TOURNAMENT_SELECTION_SIZE = 2;
    static double CULLING_RATE = 0.5;

    private static int POPULATION_SIZE = 4;
    private static int COLONY_SIZE = 2;
    private static Population population = null;
    private static Data data = new Data();

    /**
     * Main function of the driver class
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Configuring log4j
        BasicConfigurator.configure();

        // Defining default properties
        Properties defaultProps = new Properties();
        defaultProps.setProperty("populationSize", "4");
        defaultProps.setProperty("colonySize", "2");
        defaultProps.setProperty("tournamentSelectionSize", "100");
        defaultProps.setProperty("mutationRate", "0.015");
        defaultProps.setProperty("crossoverRate", "0.5");
        defaultProps.setProperty("cullingRate", "0.5");

        // Reading properties from the property file
        Properties properties = new Properties(defaultProps);
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
            POPULATION_SIZE = Integer.parseInt(properties.getProperty("populationSize"));
            COLONY_SIZE = Integer.parseInt(properties.getProperty("colonySize"));
            TOURNAMENT_SELECTION_SIZE = Integer.parseInt(properties.getProperty("tournamentSelectionSize"));
            MUTATION_RATE = Double.parseDouble(properties.getProperty("mutationRate"));
            CROSSOVER_RATE = Double.parseDouble(properties.getProperty("crossoverRate"));
            CULLING_RATE = Double.parseDouble(properties.getProperty("cullingRate"));
        } catch (IOException|NumberFormatException ex) {
            log.debug("Exception happened when reading the properties file: " + ex.getMessage());
        }

        log.info("Running algorithm with following configuration:");
        log.info("scheduling.Population Size: " + POPULATION_SIZE);
        log.info("Colony Size: " + COLONY_SIZE);
        log.info("Tournament Selection Size: " + TOURNAMENT_SELECTION_SIZE);
        log.info("Mutation Rate: " + MUTATION_RATE);
        log.info("Crossover Rate: " + CROSSOVER_RATE);
        log.info("Culling Rate: " + CULLING_RATE);

        runAlgorithm(0, POPULATION_SIZE);
    }

    /**
     * This function creates multiple colonies and run the algorithm on each colony in each thread
     * 
     * @param from Starting index of the colony in the population
     * @param to Ending index of the colony in the population
     */
    private static void runAlgorithm(int from, int to) {
        int size = to - from;

        if (size < COLONY_SIZE) {
            population = new Population(size, data, true);
        } else {
            int mid = (from + to) / 2;

            CompletableFuture<Population> colonies1 = generatePopulation(from, mid);
            CompletableFuture<Population> colonies2 = generatePopulation(mid, to);

            CompletableFuture<Population> groupColonies = colonies1
                    .thenCombine(colonies2, (xs1, xs2) -> new Population(xs1.getSchedules(), xs2.getSchedules()));

            groupColonies.whenComplete((population, throwable) -> {
                if (throwable != null) {
                    log.debug("Exception thrown in thread: " + throwable.getMessage());
                    return;
                }

                Driver.population = population;
            });

            CompletableFuture.allOf(groupColonies).join();

            groupColonies.thenRun(Driver::reproduce);
        }
    }

    /**
     * Creates a new thread for a colony
     * 
     * @param from Starting index of the colony in the population
     * @param to Ending index of the colony in the population
     * @return Created thread of type CompletableFuture
     */
    private static CompletableFuture<Population> generatePopulation(int from, int to) {
        return CompletableFuture.supplyAsync(() -> {
            runAlgorithm(from, to);
            return population;
        });
    }

    /**
     * Runs the Genetic algorithm for the population
     */
    private static void reproduce() {
        int generation = 0;
        Algorithm algorithm = new Algorithm(data);

        while (population.getSchedules().get(0).getFitness() < .5) {
            population = algorithm.evolve(population).sortByFitness();

            Schedule best = population.getSchedules().get(0);
            Schedule worst = population.getSchedules().get(population.size() - 1);

            System.out.println();
            log.info("Generation - " + (++generation));
            log.info("Best Schedule:");
            log.info(String.format("Fitness = %.5f", best.getFitness()));
            log.info("Violations = " + best.getViolations());

            log.info("Worst Schedule:");
            log.info(String.format("Fitness = %.5f", worst.getFitness()));
            log.info("Violations = " + worst.getViolations());

            log.info("\n\nBest Schedule:\n");

            log.info("Date\t\t|\tTeam1\t\t\t|\tTeam2\t\t\t|\tLocation");
            for (int i = 0; i < 120; i++)
                System.out.print("*");
            System.out.println();

            for (Match f : best.getMatchList()) {
                log.info(f);
            }
        }
    }
}
