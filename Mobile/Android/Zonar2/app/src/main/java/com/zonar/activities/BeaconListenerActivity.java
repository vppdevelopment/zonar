package com.zonar.activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.EddystoneScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.IBeaconScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.ScanContext;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.kontakt.sdk.android.manager.KontaktProximityManager;
import com.zonar.activities.com.zonar.activities.util.ItemAdapter;
import com.zonar.beacon.com.zonar.beacon.model.DataBeacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BeaconListenerActivity extends AppCompatActivity implements ProximityManager.ProximityListener {


    private static final String TAG ="POLO";

    private ProximityManagerContract proximityManager;
    private ScanContext scanContext;
    private static List beaconsList = new ArrayList<DataBeacon>();
    private ListView listView;
    private WebView webView;
    private ProgressDialog progressDialog;
    static ItemAdapter itemAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_listener);
        KontaktSDK.initialize("api-key");
        proximityManager = new KontaktProximityManager(this);

        this.listView = (ListView) findViewById(R.id.listView);
        itemAdapter = (new ItemAdapter(this, this.beaconsList));
        this.listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {


                // Loads the given URL
                DataBeacon item = (DataBeacon)

                        listView.getAdapter().getItem(position);
                webView.loadUrl(item.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(item.getUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        proximityManager.initializeScan(getScanContext(), new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.attachListener(BeaconListenerActivity.this);
            }

            @Override
            public void onConnectionFailure() {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        proximityManager.detachListener(this);
        proximityManager.disconnect();
    }

    private ScanContext getScanContext() {
        return com.zonar.beacon.ScanContext.getScannerContext();
    }

    @Override
    public void onEvent(BluetoothDeviceEvent bluetoothDeviceEvent) {
        synchronized(itemAdapter) {

            List<? extends RemoteBluetoothDevice> deviceList = bluetoothDeviceEvent.getDeviceList();
            switch (bluetoothDeviceEvent.getEventType()) {
                case SPACE_ENTERED:
                    Log.e(TAG, "namespace or region entered");
                    break;
                case DEVICE_DISCOVERED:
                    Log.e(TAG, "found new beacon");

                    for (RemoteBluetoothDevice obj : deviceList) {

                        EddystoneDevice sss = (EddystoneDevice) obj;
                        String beaconId = obj.getUniqueId();
                        DataBeacon dataBeacon = new DataBeacon(beaconId, "Aqui va nombre", sss.getUrl());

                        if(dataBeacon.getUrl() != null && !this.beaconsList.contains(dataBeacon) ) {
                            Log.d(TAG, dataBeacon.toString());
                            this.beaconsList.add(dataBeacon);
                            this.itemAdapter.notifyDataSetChanged();
                        }

                        if(dataBeacon.getUrl() == null){
                            Log.e(TAG, "BEACON NULO > "+dataBeacon.toString());
                        }
                    }
                    break;
                case DEVICES_UPDATE:
                    Log.e(TAG, "updated beacons");
                    break;
                case DEVICE_LOST:
                    Log.e(TAG, "lost device");
                    break;
                case SPACE_ABANDONED:
                    Log.e(TAG, "namespace or region abandoned");
                    break;
            }
        }
    }

    @Override
    public void onScanStart() {
        Log.e(TAG, "scan started");
    }

    @Override
    public void onScanStop() {
        Log.e(TAG, "scan stopped");
    }

    private class myWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Load the given URL on our WebView.
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // When the page has finished loading, hide progress dialog and
            // progress bar in the title.
            super.onPageFinished(view, url);
            setProgressBarIndeterminateVisibility(false);
            progressDialog.dismiss();
        }
    }
}