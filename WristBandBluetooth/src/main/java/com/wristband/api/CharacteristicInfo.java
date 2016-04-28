package com.wristband.api;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */
public class CharacteristicInfo {
    final String mServiceName;
    final String mCharacteristicName;

    CharacteristicInfo( String serviceName, String characteristicName ) {
        mServiceName = serviceName;
        mCharacteristicName = characteristicName;
    }

    public String toString() {
        return "service=" + mServiceName + " name=" + mCharacteristicName;
    }
}
