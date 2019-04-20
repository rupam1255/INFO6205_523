package scheduling;

import scheduling.pojo.Schedule;

import java.util.ArrayList;

/**
 * This class contains randomely generated collection of schedules
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */

public class Population {

    /**
     * An list of all possible schedules
     */
    private ArrayList<Schedule> schedules;

    /**
     * Parametrized constructor. Initializes with the parameters passed.
     *
     * @param size       Size of the population
     * @param data       The whole data set
     * @param initialize Indicates whether to initiate with random data
     */
    public Population(int size, Data data, boolean initialize) {
        schedules = new ArrayList(size);
        if (initialize)
            for (int i = 0; i < size; i++)
                schedules.add(new Schedule(data));
    }

    /**
     * Initialize a new population with two list of schedules
     *
     * @param s1 List 1 of schedule
     * @param s2 List 2 of schedule
     */
    public Population(ArrayList<Schedule> s1, ArrayList<Schedule> s2) {
        schedules = new ArrayList();
        schedules.addAll(s1);
        schedules.addAll(s2);
    }

    /**
     * Gets the size of this population
     *
     * @return size of the population
     */
    public int size() {
        return schedules.size();
    }

    /**
     * Add the schedule to the population
     *
     * @param s The schedule that has to be added
     */
    public void addSchedule(Schedule s) {
        schedules.add(s);
    }

    /**
     * Get all the schedules in this population
     *
     * @return List of all schedules
     */
    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * To find the schedule with the best fit
     *
     * @return the best fitting schedule
     */
    public Schedule getFittest() {
        return sortByFitness().getSchedules().get(0);
    }

    /**
     * Sort the population by fitness of each solution
     *
     * @return The instance with sorted population
     */
    public Population sortByFitness() {
        schedules.sort((schedule1, schedule2) -> {
            if (Math.abs(schedule1.getFitness() - schedule2.getFitness()) < .000000001) {
                return 0;
            } else if (schedule1.getFitness() > schedule2.getFitness()) {
                return -1;
            } else if (schedule1.getFitness() < schedule2.getFitness()) {
                return 1;
            }
            return 0;
        });
        return this;
    }

}
