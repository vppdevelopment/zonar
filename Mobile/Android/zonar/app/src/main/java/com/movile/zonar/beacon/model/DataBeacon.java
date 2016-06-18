package com.movile.zonar.beacon.model;

import com.movile.zonar.R;

/**
 * Created by Liliana on 28/04/2016.
 */
public class DataBeacon {
    private int image;
    private String url;
    private String beaconId;


    public DataBeacon(String beaconId) {
        this.image = R.mipmap.ic_launcher;
        this.beaconId = beaconId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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



    @Override
    public String toString() {
        StringBuilder sbDataBeacon = new StringBuilder();
        sbDataBeacon.append("UNIQUE ID ");
        sbDataBeacon.append(beaconId);
        sbDataBeacon.append("URL ");
        sbDataBeacon.append(url);
        return sbDataBeacon.toString();
    }

    @Override
    public boolean equals(Object o) {
        return this.getBeaconId().equals(((DataBeacon) o).getBeaconId());
    }


}
