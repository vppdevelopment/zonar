package com.zonar.integration;

import com.kontakt.sdk.android.common.model.Beacon;
import com.kontakt.sdk.android.common.model.IDevice;

import java.util.List;

/**
 * Created by Liliana on 28/04/2016.
 */
public interface KontaktIntegrator {

    public List<IDevice> getDevices(int offset);
    public Beacon getDevice(String deviceId);
}
