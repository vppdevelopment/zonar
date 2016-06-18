package com.movile.zonar.beacon;

import com.kontakt.sdk.android.common.model.Beacon;
import com.movile.zonar.beacon.model.DataBeacon;
import com.movile.zonar.integration.KontaktIntegrator;
import com.movile.zonar.integration.KontaktIntegratorImpl;

/**
 * Created by Liliana on 28/04/2016.
 */
public class BeaconControl {

    private KontaktIntegrator kontaktIntegrator;

    public BeaconControl() {
        kontaktIntegrator = new KontaktIntegratorImpl();
    }

    public DataBeacon getDataBeacon(String beaconId) {
        Beacon beacon = kontaktIntegrator.getDevice(beaconId);
        DataBeacon dataBeacon = null;
        if (beacon != null) {
            dataBeacon = new DataBeacon(beacon.getUniqueId());
        }
        return dataBeacon;
    }
}
