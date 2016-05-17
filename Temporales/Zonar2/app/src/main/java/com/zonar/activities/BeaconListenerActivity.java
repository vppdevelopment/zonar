package com.zonar.activities;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.DeviceProfile;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.kontakt.sdk.android.manager.KontaktProximityManager;
import com.zonar.activities.com.zonar.activities.util.ItemAdapter;
import com.zonar.beacon.BeaconControl;
import com.zonar.beacon.com.zonar.beacon.model.DataBeacon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BeaconListenerActivity extends AppCompatActivity implements ProximityManager.ProximityListener {

   // private static final String TAG = BeaconListenerActivity.class.getSimpleName();
    private ProximityManagerContract proximityManager;
    private static final String TAG = "POLO";

    private BeaconControl beaconControl = new BeaconControl();
    private List beaconsList = new ArrayList<DataBeacon>();
    private ListView listView;
    private WebView webView;
    private ProgressDialog progressDialog;
    ItemAdapter itemAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_listener);
        KontaktSDK.initialize("SCfoAPpNWQQBQGIpvLRciZNajCRjfeor");
        proximityManager = new KontaktProximityManager(this);
      //  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        this.listView = (ListView) findViewById(R.id.listView);
        this.webView = (WebView) findViewById(R.id.webView);
        itemAdapter = (new ItemAdapter(this, this.beaconsList));
       /*beaconsList.add(new DataBeacon("A", "Following",
                "http://www.imdb.com/title/tt0154506/"));
        beaconsList.add(new DataBeacon("B", "Following",
                "http://www.imdb.com/title/tt0154506/"));
        beaconsList.add(new DataBeacon("C", "Following",
                "http://www.imdb.com/title/tt0154506/"));
        beaconsList.add(new DataBeacon("D", "Following",
                "http://www.imdb.com/title/tt0154506/"));*/



        this.listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                // Sets the visibility of the indeterminate progress bar in the
                // title
                setProgressBarIndeterminateVisibility(true);
                // Show progress dialog
                progressDialog = ProgressDialog.show(BeaconListenerActivity.this,
                        "ProgressDialog", "Loading!");

                // Tells JavaScript to open windows automatically.
                webView.getSettings().setJavaScriptEnabled(true);
                // Sets our custom WebViewClient.
                webView.setWebViewClient(new myWebClient());
                // Loads the given URL
                DataBeacon item = (DataBeacon) listView.getAdapter().getItem(position);
                webView.loadUrl(item.getUrl());
            }
        });
    }

    private void populateViewList(){}

    @Override
    protected void onStart() {
        super.onStart();
        proximityManager.initializeScan(com.zonar.beacon.ScanContext.getScannerContext(), new OnServiceReadyListener() {
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


    @Override
    public void onEvent(BluetoothDeviceEvent bluetoothDeviceEvent) {


        List<? extends RemoteBluetoothDevice> deviceList = bluetoothDeviceEvent.getDeviceList();

        switch (bluetoothDeviceEvent.getEventType()) {
            case SPACE_ENTERED:
                break;
            case DEVICE_DISCOVERED:

                for (RemoteBluetoothDevice obj : deviceList) {
                    EddystoneDevice sss = (EddystoneDevice)obj;

String clase = obj.getClass().getName();
                    String beaconId = obj.getUniqueId();
                    DataBeacon dataBeacon = new DataBeacon(beaconId,"Aqui va nombre",sss.getUrl());
                    this.beaconsList.add(dataBeacon);
                  /*  if(beaconsList.size() ==0 || !beaconsList.contains(dataBeacon)) {
                       // dataBeacon = beaconControl.getDataBeacon(obj.getUniqueId());
                        if (dataBeacon != null) {
                            this.beaconsList.add(dataBeacon);
                           // itemAdapter.notifyDataSetChanged();
                        }
                    }*/
                }


                break;
            case DEVICES_UPDATE:
                //Log.d(TAG, "updated beacons");
                break;
            case DEVICE_LOST:

               // Log.d(TAG, "lost device");
                break;
            case SPACE_ABANDONED:
                //Log.d(TAG, "namespace or region abandoned");
                break;
        }
        this.itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScanStart() {
        itemAdapter.notifyDataSetChanged();
        Log.d(TAG, " ****************************** START *****************************");

    }

    @Override
    public void onScanStop() {
        Log.d(TAG, " ****************************** STP *****************************");
        for(Object valor: beaconsList){
            Log.d(TAG, "Beacon:");
            Log.d(TAG, valor.toString());
        }

        this.itemAdapter.notifyDataSetChanged();
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
