package com.zonar.beacon;

import com.kontakt.sdk.android.common.model.Beacon;
import com.zonar.beacon.com.zonar.beacon.model.DataBeacon;
import com.zonar.integration.KontaktIntegrator;
import com.zonar.integration.KontaktIntegratorImpl;

/**
 * Created by Liliana on 28/04/2016.
 */
public class BeaconControl {

    private KontaktIntegrator kontaktIntegrator;

    public BeaconControl() {
        kontaktIntegrator = new KontaktIntegratorImpl();
    }

    public synchronized  DataBeacon getDataBeacon(String beaconId) {
        Beacon beacon = kontaktIntegrator.getDevice(beaconId);
        DataBeacon dataBeacon = null;
        if (beacon != null) {
            dataBeacon = new DataBeacon(beacon.getUniqueId(), beacon.getName(), beacon.getUrl());

        }
        return dataBeacon;
    }
}
