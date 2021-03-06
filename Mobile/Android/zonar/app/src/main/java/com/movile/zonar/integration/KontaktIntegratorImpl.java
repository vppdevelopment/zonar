package com.movile.zonar.integration;

import android.util.Log;

import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.log.LogLevel;
import com.kontakt.sdk.android.common.model.Beacon;
import com.kontakt.sdk.android.common.model.IDevice;
import com.kontakt.sdk.android.http.HttpResult;
import com.kontakt.sdk.android.http.KontaktApiClient;
import com.kontakt.sdk.android.http.RequestDescription;
import com.kontakt.sdk.android.http.exception.ClientException;
import com.kontakt.sdk.android.http.interfaces.ResultApiCallback;
import com.movile.zonar.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liliana on 28/04/2016.
 */
public class KontaktIntegratorImpl implements KontaktIntegrator {
    private static final String TAG = KontaktIntegratorImpl.class.getSimpleName();
    private KontaktApiClient kontaktApiClient;
    private List<IDevice> allDevices;
    private Beacon beacon;

    public KontaktIntegratorImpl() {
        kontaktApiClient = new KontaktApiClient();
    }

    @Override
    public List<IDevice> getDevices(int offset) {
        allDevices = new ArrayList<>();
        fetchDevices(offset);
        return allDevices;
    }

    @Override
    public Beacon getDevice(String deviceId) {
        fetchDevice(deviceId);
        Beacon beacon = this.beacon;
        return beacon;
    }

    private void fetchDevice(String deviceId) {
        kontaktApiClient.getDevice(deviceId, new ResultApiCallback<IDevice>() {
            @Override
            public void onSuccess(HttpResult<IDevice> iDeviceHttpResult) {
                if (iDeviceHttpResult != null) {
                    beacon = iDeviceHttpResult.isPresent() ? (Beacon) iDeviceHttpResult.get() : null;
                }
            }

            @Override
            public void onFailure(ClientException e) {
                e.printStackTrace();
            }
        });
    }

    private void fetchDevices(int offset) {
        RequestDescription requestDescription = RequestDescription.start()
                .setStartIndex(offset)
                .build();

        kontaktApiClient.listDevices(requestDescription, new ResultApiCallback<List<IDevice>>() {
            @Override
            public void onSuccess(HttpResult<List<IDevice>> result) {
                Log.d(TAG, "onSuccess");
                if (result.isPresent()) {
                    allDevices.addAll(result.get());

                    int offset = result.getSearchMeta().getOffset();
                    int startIndex = result.getSearchMeta().getStartIndex();
                    if (result.getSearchMeta().hasNextResultsURI()) {
                        fetchDevices(startIndex + offset);
                    } else {
                        Log.d(TAG, "fetched devices count=" + allDevices.size());
                    }
                }
            }

            @Override
            public void onFailure(ClientException e) {
                e.printStackTrace();
            }
        });
    }
}
