package scheduling.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains single match details
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */
public class Match {

    /**
     * Member variable representing the details of a match
     */
    private Date date;
    private Team team1;
    private Team team2;
    private Location location;

    /**
     * Creates an instance of a match with values passed
     *
     * @param date     Date of the match
     * @param team1 The home team playing in the match
     * @param team2 The another team playing in the match
     * @param location The location of the match
     */
    public Match(Date date, Team team1, Team team2, Location location) {
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.location = location;
    }

    /**
     * Getter method of the {@code Date date}
     *
     * @return The value of the variable date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter method of the {@code Team team1}
     *
     * @return The value of the variable team1
     */
    public Team getTeam1() {
        return team1;
    }

    /**
     * Getter method of the {@code Team team2}
     *
     * @return The value of the variable team2
     */
    public Team getTeam2() {
        return team2;
    }

    /**
     * Getter method of the {@code Location location}
     *
     * @return The value of the variable location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Convert the object into a String
     *
     * @return String equivalent of the Object
     */
    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return String.format("%s\t|\t%s\t|\t%s\t|\t%s", df.format(date), fillToLengthWithSpaces(team1.toString(), 30),
                fillToLengthWithSpaces(team2.toString(), 30), location);
    }

    /**
     * A helper to change a string's length by suffixing empty spaces
     *
     * @param s The string to be formatted
     * @param l Number of characters required
     * @return The formatted string
     */
    private String fillToLengthWithSpaces(String s, int l) {
        StringBuilder sBuilder = new StringBuilder(s);
        for (int i = 0; i < l; i++) {
            if (i >= sBuilder.length())
                sBuilder.append(" ");
        }
        return sBuilder.toString();
    }
}
