package com.njupt.sniper.smartparking.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/3.
 */
public class ParkingLocation implements Serializable {
    private String name;
    private Integer id;
    private float xLocation;
    private float yLocation;
    private int nub;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getxLocation() {
        return xLocation;
    }

    public void setxLocation(float xLocation) {
        this.xLocation = xLocation;
    }

    public float getyLocation() {
        return yLocation;
    }

    public void setyLocation(float yLocation) {
        this.yLocation = yLocation;
    }

    public int getNub() {
        return nub;
    }

    public void setNub(int nub) {
        this.nub = nub;
    }
}
