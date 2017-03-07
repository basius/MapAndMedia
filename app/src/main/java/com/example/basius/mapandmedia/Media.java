package com.example.basius.mapandmedia;

/**
 * Created by basius on 7/03/17.
 */

public class Media {
    public String name;
    public String type;
    public String absolutePath;
    public String lat;
    public String lon;

    public Media() {
    }

    public Media(String name, String type, String absolutePath, String lat, String lon) {
        this.name = name;
        this.type = type;
        this.absolutePath = absolutePath;
        this.lat = lat;
        this.lon = lon;
    }
}
