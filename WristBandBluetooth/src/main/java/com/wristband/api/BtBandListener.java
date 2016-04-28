package com.wristband.api;

import java.util.Date;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */
public interface BtBandListener {

    void onAHServiceReadAccData( int[] data );
    void onAHServiceReadCurrentTime( int bandSeconds, Date deviceDate, long deltaMs );
    void onAHServiceReadData( );
    void onAHServiceReadEvent( BtBandEvent event );
    void onAHServiceReadMode( BandMode.AccessoryMode mode );
    void onAHServiceReadProtocolVersion( int version );
    void onAHServiceReadUptime( int uptime, Date started );

    void onBatteryLevel( int level );

    void onFirmwareRevision( String rev );
    void onSoftwareRevision( String rev );
    void onHardwareRevision( String rev );
    void onManufacturerName( String name );

    void onAASDeviceVersion( int version );
    void onAASDeviceSmartLink( int version );
    void onAASDeviceProductId( String productId );
    void onAASDeviceData( String data );

    void onGADeviceName( String data );

    void onConnectionStateChange( BtBridge.Status status );
    void onServicesDiscovered();
}
