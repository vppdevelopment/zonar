package com.zonar.beacon;

import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.EddystoneScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.IBeaconScanContext;
import com.kontakt.sdk.android.ble.manager.ProximityManager;

import java.util.concurrent.TimeUnit;


public class ScanContext {
    private static com.kontakt.sdk.android.ble.configuration.scan.ScanContext scanContext;

    public static com.kontakt.sdk.android.ble.configuration.scan.ScanContext getScannerContext() {
        if (scanContext == null) {
            createScanContext();
        }
        return scanContext;
    }

    private static void createScanContext() {
        if (scanContext == null) {
            scanContext = new com.kontakt.sdk.android.ble.configuration.scan.ScanContext.Builder()
                    //.setScanPeriod(ScanPeriod.RANGING) // or for monitoring for 15 seconds scan and 10 seconds waiting:
                    .setScanPeriod(new ScanPeriod(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(10)))
                    .setScanMode(ProximityManager.SCAN_MODE_BALANCED)
                    .setActivityCheckConfiguration(ActivityCheckConfiguration.MINIMAL)
                    .setForceScanConfiguration(ForceScanConfiguration.MINIMAL)
                    .setIBeaconScanContext(new IBeaconScanContext.Builder().build())
                    .setEddystoneScanContext(new EddystoneScanContext.Builder().build())
                    .setForceScanConfiguration(ForceScanConfiguration.MINIMAL)
                    .build();

        }
    }
}
