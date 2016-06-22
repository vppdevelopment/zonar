package com.movile.zonar.service;

import com.movile.zonar.beacon.model.DataBeacon;
import com.movile.zonar.integration.ApiCoreIntegrator;
import com.movile.zonar.integration.ApiCoreIntegratorImpl;

import java.util.HashMap;
import java.util.Map;


public class BeaconListService {
    private static final String MESSAGE_PARAM_NAME = "message";

    private ApiCoreIntegrator apiCoreIntegrator = new ApiCoreIntegratorImpl();

    public DataBeacon getBeaconContent(String url, DataBeacon dataBeacon) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(MESSAGE_PARAM_NAME, dataBeacon.getBeaconId());
        String urlContent = apiCoreIntegrator.get(url, parameters);
        dataBeacon.setUrl(urlContent.replace("\"",""));
        return dataBeacon;
    }
}
