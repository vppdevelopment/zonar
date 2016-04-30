package com.movile.zonar.beacon.model;

/**
 * Created by Liliana on 28/04/2016.
 */
public class DataBeacon {
    private int image;
    private String url;
    private String name;
    private String beaconId;

    public DataBeacon(String beaconId, String name, String url) {
        this.url = url;
        this.name = name;
        this.beaconId = beaconId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String toString() {
        StringBuilder sbDataBeacon = new StringBuilder();
        sbDataBeacon.append("NAME ");
        sbDataBeacon.append(name);
        sbDataBeacon.append("UNIQUE ID ");
        sbDataBeacon.append(beaconId);
        sbDataBeacon.append("URL ");
        sbDataBeacon.append(url);
        return sbDataBeacon.toString();
    }
}
