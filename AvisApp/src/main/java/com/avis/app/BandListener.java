package com.avis.app;

import java.util.Date;

import com.wristband.api.BandMode;
import com.wristband.api.BtBandEvent;
import com.wristband.api.BtBandListener;
import com.wristband.api.BtBridge;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * MIT Licensed
 */
public class BandListener implements BtBandListener {
    private BandHandler bandHandler;

    interface BandHandler{
        void onAccData(float x, float y, float z);
        void onBandConnection(boolean connected);
        void onButtonClick();
    }

    public BandListener(BandHandler bandHandler){
        this.bandHandler = bandHandler;
    }
    @Override
    public void onAHServiceReadAccData(int[] data) {
        float x = convertAxleValue(data[0]);
        float y = convertAxleValue(data[1]);
        float z = convertAxleValue(data[2]);
        bandHandler.onAccData(x, y, z);
    }

    public static final float GRAVITY_FACTOR = 101.97162f;

    private float convertAxleValue(int data) {
        if ((data & 0x200) != 0) {
            data = -((data ^ 1023) + 1);
        }
        return (((float) (-data)) * 15.625f) / GRAVITY_FACTOR;
    }

    @Override
    public void onAHServiceReadCurrentTime(int bandSeconds, Date deviceDate, long deltaMs) {

    }

    @Override
    public void onAHServiceReadData() {

    }

    @Override
    public void onAHServiceReadEvent(BtBandEvent event) {
        if(event.getType() == BtBandEvent.EventType.BUTTON){
            bandHandler.onButtonClick();
        }
    }

    @Override
    public void onAHServiceReadMode(BandMode.AccessoryMode mode) {

    }

    @Override
    public void onAHServiceReadProtocolVersion(int version) {

    }

    @Override
    public void onAHServiceReadUptime(int uptime, Date started) {

    }

    @Override
    public void onBatteryLevel(int level) {

    }

    @Override
    public void onFirmwareRevision(String rev) {

    }

    @Override
    public void onSoftwareRevision(String rev) {

    }

    @Override
    public void onHardwareRevision(String rev) {

    }

    @Override
    public void onManufacturerName(String name) {

    }

    @Override
    public void onAASDeviceVersion(int version) {

    }

    @Override
    public void onAASDeviceSmartLink(int version) {

    }

    @Override
    public void onAASDeviceProductId(String productId) {

    }

    @Override
    public void onAASDeviceData(String data) {

    }

    @Override
    public void onGADeviceName(String data) {

    }

    @Override
    public void onConnectionStateChange(BtBridge.Status status) {
        if(status == BtBridge.Status.CONNECTED){
            bandHandler.onBandConnection(true);
        }else if(status == BtBridge.Status.DISCONNECTED){
            bandHandler.onBandConnection(false);
        }
    }

    @Override
    public void onServicesDiscovered() {

    }
}
