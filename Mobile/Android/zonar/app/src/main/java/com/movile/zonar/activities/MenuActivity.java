package com.movile.zonar.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
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

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks,
        ProximityManager.ProximityListener {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static final String TAG = MenuActivity.class.getSimpleName();
    private ProximityManagerContract proximityManager;
    private BeaconControl beaconControl;
    private List beaconsList = new ArrayList<DataBeacon>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        KontaktSDK.initialize("SCfoAPpNWQQBQGIpvLRciZNajCRjfeor").setDebugLoggingEnabled(BuildConfig.DEBUG)
                .setLogLevelEnabled(LogLevel.DEBUG, true);
        proximityManager = new KontaktProximityManager(this);
        beaconControl= new BeaconControl();

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
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

    @Override
    public void onEvent(BluetoothDeviceEvent bluetoothDeviceEvent) {
        List<? extends RemoteBluetoothDevice> deviceList = bluetoothDeviceEvent.getDeviceList();

        switch (bluetoothDeviceEvent.getEventType()) {
            case SPACE_ENTERED:
                break;
            case DEVICE_DISCOVERED:
                for (RemoteBluetoothDevice obj : deviceList) {
                    String name = obj.getName();
                    Double distance = obj.getDistance();
                    DataBeacon dataBeacon = BuildDataBeacon(obj);
                    if (dataBeacon != null) {
                        this.beaconsList.add(dataBeacon);
                    }
                }
                for(Object valor: beaconsList){
                    Log.d(TAG, "found new beacon");
                    Log.d(TAG, valor.toString());
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
}
