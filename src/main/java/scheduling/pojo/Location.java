package scheduling.pojo;

import java.util.Objects;

/**
 * This class contains location for matches
 *
 * @author Rupam Tiwari, Utkarsha Pampatwar
 * @version 1.0
 * @Date 04/18/2019
 */
public class Location {
    /**
     * Location of the match
     */
    private String locationName;

    /**
     * Creates an instance with passed location
     *
     * @param locationName Name of the location
     */
    public Location(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Getter method for {@code String locationName}
     *
     * @return The value of the variable locationName
     */
    public String getLocationName() {
        return locationName;
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
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(locationName, location.locationName);
    }

    /**
     * Generates hashcode for this instance of object
     *
     * @return hashcode of the object
     */
    @Override
    public int hashCode() {

        return Objects.hash(locationName);
    }

    /**
     * Convert the object into a String
     *
     * @return String equivalent of the Object
     */
    @Override
    public String toString() {
        return locationName;
    }
}
