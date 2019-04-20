package scheduling.pojo;

import java.util.Objects;

/**
 * This class contains team information
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */
public class Team {
    /**
     * Member variable representing the Team. Their name and their home-ground
     */
    private String name;
    private Location homeGround;

    /**
     * Creates an instance of a team with the passed parameters
     *
     * @param name       Name of the team
     * @param homeGround Home ground of the team
     */
    public Team(String name, Location homeGround) {
        this.homeGround = homeGround;
        this.name = name;
    }

    /**
     * Getter method for {@code String name}
     *
     * @return The name of the team
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for {@code Location homeGround}
     *
     * @return The home ground of the team
     */
    public Location getHomeGround() {
        return homeGround;
    }

    /**
     * Checks for equality
     *
     * @param o Another object
     * @return true if both are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) &&
                Objects.equals(homeGround, team.homeGround);
    }

    /**
     * Generates hashcode for this instance of object
     *
     * @return hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, homeGround);
    }

    /**
     * Convert the object into a String
     *
     * @return String equivalent of the Object
     */
    @Override
    public String toString() {
        return name;
    }
}
