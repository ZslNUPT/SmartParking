package com.njupt.sniper.smartparking.api.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/3.
 */
public class ParkingLocationEntity implements Serializable {

    private String title;
    private String address;
    private int id;

    private double xLocation;
    private double yLocation;
    private int nub;

    public ParkingLocationEntity(int id,double xLocation, double yLocation, int nub) {
        this.id=id;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.nub = nub;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getxLocation() {
        return xLocation;
    }

    public void setxLocation(double xLocation) {
        this.xLocation = xLocation;
    }

    public double getyLocation() {
        return yLocation;
    }

    public void setyLocation(double yLocation) {
        this.yLocation = yLocation;
    }

    public int getNub() {
        return nub;
    }

    public void setNub(int nub) {
        this.nub = nub;
    }
}
