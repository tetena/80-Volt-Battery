package com.eightyvoltbattery.grooveradar;

/**
 * An ArcadeEntry stores the data for one particular arcade.
 */
public class ArcadeEntry {

    private String name_;
    private String address_;
    private double distanceFromUser_;

    /**
     * Creates a new ArcadeEntry with the given name and address.
     *
     * @param name The arcade's name.
     * @param address The arcade's address.
     */
    public ArcadeEntry(String name, String address) {
        name_ = name;
        address_ = address;
    }

    /**
     * Sets the distance from the user's current location to this arcade, in miles.
     *
     * @param distanceFromUser
     */
    void setDistanceFromUser(double distanceFromUser) {
        distanceFromUser_ = distanceFromUser;
    }

    /**
     * Returns the name of this arcade.
     *
     * @return The name of this arcade.
     */
    String getName() {
        return name_;
    }

    /**
     * Returns the address of this arcade.
     *
     * @return The address of this arcade.
     */
    String getAddress() {
        return address_;
    }

    /**
     * Returns the distance from the user's location to this arcade, in miles.
     *
     * @return The distance from the user's location to this arcade, in miles.
     */
    double getDistanceFromUser() {
        return distanceFromUser_;
    }
}