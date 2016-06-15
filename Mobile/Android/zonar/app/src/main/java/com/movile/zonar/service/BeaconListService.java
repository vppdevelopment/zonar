package com.movile.zonar.service;

import com.movile.zonar.beacon.model.DataBeacon;
import com.movile.zonar.integration.ApiCoreIntegrator;
import com.movile.zonar.integration.ApiCoreIntegratorImpl;

/**
 * Created by marortiz2 on 6/15/16.
 */
public class BeaconListService {
    private ApiCoreIntegrator apiCoreIntegrator = new ApiCoreIntegratorImpl();
    public DataBeacon getBeaconContent(String beaconId){
        String urlContent = apiCoreIntegrator.get("http://vppdevelopment.pythonanywhere.com/message/YDFS",null,null);
        DataBeacon dataBeacon = new DataBeacon(beaconId,beaconId,urlContent);
        return dataBeacon;
    }
}
