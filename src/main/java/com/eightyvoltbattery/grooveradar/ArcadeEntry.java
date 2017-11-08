package com.eightyvoltbattery.grooveradar;

/**
 * An ArcadeEntry stores the data for one particular arcade.
 */
public class ArcadeEntry {

    private int id_;
    private String name_;
    private String phoneNumber_;
    private String address_;
    private String hours_;
    private String info_;
    private double distanceFromUser_;

    /**
     * Creates a new ArcadeEntry with the given name and address.
     *
     * @param name The arcade's name.
     * @param address The arcade's address.
     */
    public ArcadeEntry(int id, String name, String phoneNumber, String address, String hours, String info) {
        id_ = id;
        name_ = name;
        phoneNumber_ = phoneNumber;
        address_ = address;
        hours_ = hours;
        info_ = info;
    }

    /**
     * Sets the distance from the user's current location to this arcade, in miles.
     *
     * @param distanceFromUser
     */
    void setDistanceFromUser(double distanceFromUser) {
        distanceFromUser_ = distanceFromUser;
    }

    int getId() { return id_; }

    String getPhoneNumber() { return phoneNumber_; }

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

    String getHours() { return hours_; }

    String getInfo() { return info_; }

    /**
     * Returns the distance from the user's location to this arcade, in miles.
     *
     * @return The distance from the user's location to this arcade, in miles.
     */
    double getDistanceFromUser() {
        return distanceFromUser_;
    }
}