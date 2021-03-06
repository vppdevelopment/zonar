package com.movile.zonar.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.log.LogLevel;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.kontakt.sdk.android.manager.KontaktProximityManager;
import com.movile.zonar.BuildConfig;
import com.movile.zonar.R;
import com.movile.zonar.beacon.BeaconControl;
import com.movile.zonar.beacon.ScanContext;
import com.movile.zonar.beacon.model.DataBeacon;
import com.movile.zonar.dialog.Navigator;
import com.movile.zonar.integration.ApiCoreIntegrator;
import com.movile.zonar.integration.ApiCoreIntegratorImpl;
import com.movile.zonar.service.BeaconListService;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks,
        ProximityManager.ProximityListener {
    private  String CORE_API_URL;
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static final String TAG = "POLO";
    private ProximityManagerContract proximityManager;
    private ScanContext scanContext;

    private List beaconsList = new ArrayList<DataBeacon>();
    private ListView listView;
    private WebView webView;
    public ProgressDialog progressDialog;
    ItemAdapter itemAdapter;

    private BeaconListService beaconListService = new BeaconListService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        CORE_API_URL = getResources().getString(R.string.core_url);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        KontaktSDK.initialize("SCfoAPpNWQQBQGIpvLRciZNajCRjfeor").setDebugLoggingEnabled(BuildConfig.DEBUG).setLogLevelEnabled(LogLevel.DEBUG, true);
        proximityManager = new KontaktProximityManager(this);
        this.listView = (ListView) findViewById(R.id.listView);
        beaconsList = new ArrayList<DataBeacon>();
        //beaconsList.add(new DataBeacon("A", "Following", "http://www.imdb.com/title/tt0154506/"));
        itemAdapter = (new ItemAdapter(this, this.beaconsList));
        this.listView.setAdapter(itemAdapter);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {
                DataBeacon item = (DataBeacon) listView.getAdapter().getItem(position);
                setProgressBarIndeterminateVisibility(true);
                progressDialog = ProgressDialog.show(MenuActivity.this,
                        "ProgressDialog", "Loading!");
                Navigator navigatorDialog = new Navigator();
                navigatorDialog.setUrl(item.getUrl());

                navigatorDialog.setActivity(MenuActivity.this);
                navigatorDialog.show(getFragmentManager(), "Navigator");
                //dlg2.getShowsDialog();
                // Loads the given URL
                //  webView.loadUrl(item.getUrl());
                // Intent intent = new Intent(Intent.ACTION_VIEW);

                // myWebView.loadUrl(item.getUrl());
            }
        });

        //itemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        proximityManager.initializeScan(ScanContext.getScannerContext(), new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.attachListener(MenuActivity.this);
            }

            @Override
            public void onConnectionFailure() {
            }
        });

    }

    @Override
    public void onScanStop() {
        Log.d(TAG, "scan stopped");
    }

    private com.kontakt.sdk.android.ble.configuration.scan.ScanContext getScanContext() {
        return ScanContext.getScannerContext();
    }

    @Override
    public void onEvent(BluetoothDeviceEvent bluetoothDeviceEvent) {

        synchronized (itemAdapter) {

            List<? extends RemoteBluetoothDevice> deviceList = bluetoothDeviceEvent.getDeviceList();
            switch (bluetoothDeviceEvent.getEventType()) {
                case SPACE_ENTERED:
                    break;
                case DEVICE_DISCOVERED:
                    for (RemoteBluetoothDevice obj : deviceList) {
                        DataBeacon dataBeacon = BuildDataBeacon(obj);
                        if (dataBeacon != null) {
                            if (!this.beaconsList.contains(dataBeacon)) {
                                LongRunningGetIO backgroundProcess = new LongRunningGetIO();
                                backgroundProcess.setDataBeacon(dataBeacon);
                                 backgroundProcess.execute();
                            }
                        }
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
        }
    }

    private DataBeacon BuildDataBeacon(RemoteBluetoothDevice obj) {
        EddystoneDevice data = (EddystoneDevice) obj;
        DataBeacon beacon = null;
        if (obj.getUniqueId() != null) {
            beacon = new DataBeacon(obj.getUniqueId());
        }
        return beacon;
    }

    @Override
    public void onScanStart() {
        Log.d(TAG, "scan started");
    }

    @Override
    protected void onStop() {
        super.onStop();

        proximityManager.detachListener(this);
        proximityManager.disconnect();

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



    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
private DataBeacon dataBeacon = null;



        @Override
        protected String doInBackground(Void... params) {
            if(dataBeacon != null) {
                beaconListService.getBeaconContent(CORE_API_URL, dataBeacon);
                return dataBeacon.getUrl();
            }
            return null;
        }


        protected void onPostExecute(String results) {
            if(dataBeacon != null){
                Log.d(TAG, dataBeacon.toString());
                beaconsList.add(dataBeacon);
                itemAdapter.notifyDataSetChanged();
            }
        }

        public DataBeacon getDataBeacon() {
            return dataBeacon;
        }

        public void setDataBeacon(DataBeacon dataBeacon) {
            this.dataBeacon = dataBeacon;
        }
    }


}
