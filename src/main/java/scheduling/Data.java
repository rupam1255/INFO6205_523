package scheduling;

import scheduling.pojo.Location;
import scheduling.pojo.Team;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class holds the complete data set for the algorithm to be applied.
 * This class contains the following:
 * - The teams which are playing for this season
 * - Possible dates the matches can be scheduled
 * - Possible locations where the matches can be scheduled
 * - Weather condition for each day at each location (Probability for Rain)

    **@author Rupam Tiwari,Utkarsha Pampatwar
    **@version 1.0
    **@Date 04/18/2019
    **/
public class Data {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    /**
     * The member variables for holding the data
     */
    private ArrayList<Date> dates;
    private ArrayList<Team> teamList;
    private ArrayList<Location> locationList;
    private HashMap<String, HashMap<Location, Integer>> weather;

    /**
     * Constructor. Creates an instance for the class scheduling.Data.
     */
    public Data() {
        dates = new ArrayList<>();
        teamList = new ArrayList<>();
        locationList = new ArrayList<>();
        weather = new HashMap<>();
        initializeData();
    }

    /**
     * Getter method for {@code ArrayList<Date> dates}
     *
     * @return The value of dates variable
     */
    public ArrayList<Date> getDates() {
        return dates;
    }

    /**
     * Getter method for {@code ArrayList<Team> teamList}
     *
     * @return The value of teamList variable
     */
    public ArrayList<Team> getTeamList() {
        return teamList;
    }

    /**
     * Getter method for {@code ArrayList<Location> locationList}
     *
     * @return The value of locationList variable
     */
    public ArrayList<Location> getLocationList() {
        return locationList;
    }

    /**
     * Getter method for {@code HashMap<Date, HashMap<Location, Integer>> weather}
     *
     * @return The value of weather variable
     */
    public HashMap<String, HashMap<Location, Integer>> getWeather() {
        return weather;
    }

    /**
     * This function initializes all the member variable with default values. In this case the data.
     */
    void initializeData() {
        Location location1 = new Location("England");
        Location location2 = new Location("India");
        Location location3 = new Location("SouthAfrica");
        Location location4 = new Location("Bangladesh");

        locationList.clear();
        locationList.addAll(Arrays.asList(location1, location2, location3, location4));

        Team team1 = new Team("EnglandTeam", location1);
        Team team2 = new Team("India", location2);
        Team team3 = new Team("SouthAfrica", location3);
        Team team4 = new Team("Bangladesh", location4);

        teamList.clear();
        teamList.addAll(Arrays.asList(team1, team2, team3, team4));

        int count = 0;
        Date currDate = new Date();
        Calendar today = Calendar.getInstance();
        today.setTime(currDate);
        dates.clear();
        while (count <= (teamList.size() * teamList.size())) {
            dates.add(today.getTime());
            today.add(Calendar.DAY_OF_YEAR, 1);
            count++;
        }

        weather.clear();
        for (Date d : dates) {
            HashMap<Location, Integer> map = new HashMap();
            weather.put(dateFormat.format(d), map);
            for (Location l : locationList) {
                map.put(l, (new Random()).nextInt(100));
            }
        }
    }
}