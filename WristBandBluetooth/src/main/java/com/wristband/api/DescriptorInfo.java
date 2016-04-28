package com.wristband.api;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */
public class DescriptorInfo {
    final String mServiceName;
    final String mCharacteristicName;
    final String mDescriptorName;

    DescriptorInfo( String serviceName, String characteristicName, String descriptorName ) {
        mServiceName = serviceName;
        mCharacteristicName = characteristicName;
        mDescriptorName = descriptorName;
    }

    public String toString() {
        return "service=" + mServiceName + " name=" + mCharacteristicName + " descriptor=" + mDescriptorName;
    }
}
