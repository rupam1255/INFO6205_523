package scheduling.pojo;

import scheduling.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * This class contains schedule of all matches in one tournament
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */

public class Schedule {

    /**
     * This hash-map stores the probability of rain for each day at each location
     */
    private HashMap<String, HashMap<Location, Integer>> weatherIndex;

    /**
     * This stores the number of matches played by each team
     */
    private HashMap<Team, Integer> matchesPlayed;

    /**
     * This stores the number of matches played in home-ground by each team
     */
    private HashMap<Team, Integer> homeMatches;

    /**
     * This stores the number of matches played at each location
     */
    private HashMap<Location, Integer> matchesInLocation;

    /**
     * This stores the list of all fixtures in this schedule
     */
    private ArrayList<Match> matchList;

    /**
     * This stores the fitness of this schedule
     */
    private Double fitness;

    /**
     * This stores the total number of violations in this schedule
     */
    private int violations;

    /**
     * scheduling.Data variable to store all the data
     */
    private Data data;

    /**
     * Creates an instance of Schedule based on the data sent
     *
     * @param data scheduling.Data set using which the schedule has to be made
     */
    public Schedule(Data data) {
        fitness = (double) -1;
        matchList = new ArrayList<>();
        matchesPlayed = new HashMap<>();
        homeMatches = new HashMap<>();
        matchesInLocation = new HashMap<>();
        this.data = data;
        initialize();
    }

    /**
     * Creates an instance with empty data. Used when creating test cases, where data has to be filled manually
     */
    public Schedule() {
        fitness = (double) -1;
        matchList = new ArrayList<>();
        matchesPlayed = new HashMap<>();
        homeMatches = new HashMap<>();
        matchesInLocation = new HashMap<>();
        data = null;
    }

    /**
     * This method initializes schedule randomly based on the total data-set
     */
    private void initialize() {
        weatherIndex = data.getWeather();
        for (Team team : data.getTeamList()) {
            homeMatches.put(team, 0);
            matchesPlayed.put(team, 0);
        }
        for (Location location : data.getLocationList()) {
            matchesInLocation.put(location, 0);
        }

        int totalFixtures = data.getTeamList().size() * (data.getTeamList().size() - 1);

        for (int i = 0; i < totalFixtures; i++) {
            Team homeTeam = data.getTeamList().get((int) (data.getTeamList().size() * Math.random()));
            Team opponent;
            while ((opponent = data.getTeamList().get((int) (data.getTeamList().size() * Math.random()))).equals(homeTeam));
            Date d = data.getDates().get((int) (data.getDates().size() * Math.random()));

            matchList.add(new Match(d, homeTeam, opponent, homeTeam.getHomeGround()));
        }

        sortFixturesByDate();
    }

    /**
     * This function sorts the fixtures by the match's date
     */
    public void sortFixturesByDate() {
        matchList.sort((f1, f2) -> {
            if (f1.getDate().after(f2.getDate()))
                return 1;
            else if (f1.getDate().before(f2.getDate()))
                return -1;
            else
                return 0;
        });
    }

    /**
     * Getter method for {@code matchList}
     *
     * @return List of all fixtures in this schedule
     */
    public ArrayList<Match> getMatchList() {
        return matchList;
    }

    /**
     * This method calculates and returns the fitness of this schedule
     *
     * @return fitness of this schedule
     */
    public Double getFitness() {
        fitness = computeFitness();
        return fitness;
    }

    /**
     * This method can be used to get the total number of violations in this schedule
     *
     * @return number of violations
     */
    public int getViolations() {
        return violations;
    }

    /**
     * Setter method for {@code scheduling.Data data}
     *
     * @param data The data that has to be used for this schedule
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * Setter method for the variable {@code HashMap<String, HashMap<Location, Integer>> weatherIndex}
     *
     * @param weatherIndex The HashMap of weather index (Probability for bad weather)
     */
    public void setWeatherIndex(HashMap<String, HashMap<Location, Integer>> weatherIndex) {
        this.weatherIndex = weatherIndex;
    }

    /**
     * This method gives the total number of fixtures in this schedule
     *
     * @return size of the fixtures list
     */
    public int size() {
        return matchList.size();
    }

    /**
     * This method calculates the fitness of this schedule with respect to the criteria needed.
     *
     * @return fitness of this schedule
     */
    public double computeFitness() {
        violations = 0;

        matchesPlayed.clear();
        matchesInLocation.clear();
        homeMatches.clear();
        for (int i = 0; i < matchList.size(); i++) {
            Match f1 = matchList.get(i);

            // Adding penalties for scheduling matches where the probability of raining is high
            int weatherIndex = this.weatherIndex.get(Data.dateFormat.format(f1.getDate())).get(f1.getLocation());
            if (weatherIndex > 70){
                violations++;
//                if(violations == 1)
//                    System.out.println("Bad Weather violation");
            }

            // Calculation of total matches played by each team
            if (matchesPlayed.containsKey(f1.getTeam1()))
                matchesPlayed.put(f1.getTeam1(), matchesPlayed.get(f1.getTeam1()) + 1);
            else
                matchesPlayed.put(f1.getTeam1(), 1);

            if (matchesPlayed.containsKey(f1.getTeam2()))
                matchesPlayed.put(f1.getTeam2(), matchesPlayed.get(f1.getTeam2()) + 1);
            else
                matchesPlayed.put(f1.getTeam2(), 1);

            // Calculation of total matches played by each location
            if (matchesInLocation.containsKey(f1.getLocation()))
                matchesInLocation.put(f1.getLocation(), matchesInLocation.get(f1.getLocation()) + 1);
            else
                matchesInLocation.put(f1.getLocation(), 1);

            // Calculation of total matches played at home ground
            if (homeMatches.containsKey(f1.getTeam1()))
                homeMatches.put(f1.getTeam1(), homeMatches.get(f1.getTeam1()) + 1);
            else
                homeMatches.put(f1.getTeam1(), 1);

            // Two matches should not happen on the same day
            Match f2;
            int j = 0;
            while ((i + (++j)) < matchList.size()) {
                f2 = matchList.get(i + j);
                if (Data.dateFormat.format(f1.getDate()).equals(Data.dateFormat.format(f2.getDate()))) {
                    violations++;
//                    if(violations == 1)
//                        System.out.println("Two match Same Day violation");
                } else {
                    break;
                }
            }

            // Computing next day
            Date currentDate = f1.getDate();
            LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            localDateTime = localDateTime.plusDays(1);
            Date currentDatePlusOneDay = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // Same team should not play on consecutive dates
            j = 0;
            while ((i + (++j)) < matchList.size()) {
                f2 = matchList.get(i + j);
                if (Data.dateFormat.format(currentDatePlusOneDay).equals(Data.dateFormat.format(f2.getDate()))) {
                    if (f1.getTeam2().equals(f2.getTeam2()) ||
                            f1.getTeam2().equals(f2.getTeam1()) ||
                            f1.getTeam1().equals(f2.getTeam1()) ||
                            f1.getTeam1().equals(f2.getTeam2())) {
                        violations++;
//                        if(violations == 1)
//                            System.out.println("Consecutive Day violation");
                    }
                } else {
                    break;
                }
            }

            // Two matches should not be same
            j = 0;
            while ((i + (++j)) < matchList.size()) {
                f2 = matchList.get(i + j);
                if (f1.getTeam1().equals(f2.getTeam1()) && f1.getTeam2().equals(f2.getTeam2()))
                    violations++;
//                if(violations == 1)
//                    System.out.println("Same match violation");
            }
        }

        // Each team should have played 2 matches with each other teams
        Iterator<Map.Entry<Team, Integer>> entries = matchesPlayed.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Team, Integer> entry = entries.next();

            if (entry.getValue() != (2 * data.getTeamList().size()) - 2) {
                violations++;
//                if(violations == 1)
//                    System.out.println("Each Team Played 2 match with each other violation");
            }
        }

        // Each location should have hosted exactly one match for each team
        Iterator<Map.Entry<Location, Integer>> entries2 = matchesInLocation.entrySet().iterator();
        while (entries2.hasNext()) {
            Map.Entry<Location, Integer> entry2 = entries2.next();

            if (entry2.getValue() != (data.getTeamList().size() - 1)) {
                violations++;
//                if(violations == 1)
//                    System.out.println("each location host one match per team violation");
            }
        }

        // Each team should have played exactly one match against each team on their home ground
        Iterator<Map.Entry<Team, Integer>> entries3 = homeMatches.entrySet().iterator();
        while (entries3.hasNext()) {
            Map.Entry<Team, Integer> entry3 = entries3.next();

            if (entry3.getValue() != (data.getTeamList().size() - 1)) {
                violations++;
//                if(violations == 1)
//                    System.out.println("one match per team at homeground violation");
            }
        }

        return (double) 1 / (1 + violations);
    }
}
