package com.wristband.api;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */
public interface BtBridgeListener {
    void onCharacteristicRead( BluetoothGattCharacteristic characteristic );
    void onConnectionStateChange( BtBridge.Status status );
    void onCharacteristicChanged( BluetoothGattCharacteristic characteristic );
    void onCharacteristicWrite( BluetoothGattCharacteristic characteristic );
    void onDescriptorRead( BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor );
    void onDescriptorWrite( BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor );
    void onServicesDiscovered( List<BluetoothGattService> services );

}
