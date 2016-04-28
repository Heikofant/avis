package com.avis.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * MIT Licensed
 */

public class LocListener implements LocationListener{
    interface LocationHandler{
        void onLocation(double long_, double lat_);
    }

    private LocationHandler handler;
    public LocListener(LocationHandler handler){
        this.handler = handler;
    }

    @Override
    public void onLocationChanged(Location location) {
        handler.onLocation(location.getLongitude(), location.getLatitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
