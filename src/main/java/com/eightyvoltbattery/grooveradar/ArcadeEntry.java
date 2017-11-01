package com.eightyvoltbattery.grooveradar;

public class ArcadeEntry {

    private String name_;
    private String address_;
    private double distanceFromUser_;

    public ArcadeEntry(String name, String address) {
        name_ = name;
        address_ = address;
    }

    void setDistanceFromUser(double distanceFromUser) {
        distanceFromUser_ = distanceFromUser;
    }

    String getName() {
        return name_;
    }

    String getAddress() {
        return address_;
    }

    double getDistanceFromUser() {
        return distanceFromUser_;
    }
}
