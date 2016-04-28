package com.wristband.api;

import android.util.Log;

import java.util.HashMap;
import java.util.UUID;

import com.wristband.api.profiles.AADeviceServiceProfile;
import com.wristband.api.profiles.AHServiceProfile;
import com.wristband.api.profiles.BaseProfile;
import com.wristband.api.profiles.BatteryProfile;
import com.wristband.api.profiles.DeviceInformationProfile;
import com.wristband.api.profiles.GenericAccessProfile;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */

public class Profiles {
    private static final String CLASS = "Profiles";
    private static HashMap<UUID, BaseProfile> sUUIDProfileMap;

    static {
        sUUIDProfileMap = new HashMap();
        sUUIDProfileMap.put( BatteryProfile.SERVICE_UUID          , new BatteryProfile()           );
        sUUIDProfileMap.put( AHServiceProfile.SERVICE_UUID        , new AHServiceProfile()         );
        sUUIDProfileMap.put( AADeviceServiceProfile.SERVICE_UUID  , new AADeviceServiceProfile()   );
        sUUIDProfileMap.put( DeviceInformationProfile.SERVICE_UUID, new DeviceInformationProfile() );
        sUUIDProfileMap.put( GenericAccessProfile.SERVICE_UUID    , new GenericAccessProfile()     );
    }

    public static BaseProfile getService( UUID uuid ) {
        Log.v( CLASS, "getService: uuid=" + uuid.toString() );
        Object profile = sUUIDProfileMap.get( uuid );
        if( profile == null ) {
            Log.w( CLASS, "getService: Profile not found for uuid=" + uuid.toString() );
            return null;
        }

        return ( BaseProfile ) profile;
    }
}
