package com.wristband.api;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */

public class ADroneNotifConfig implements Parcelable {
    public static final Creator<ADroneNotifConfig> CREATOR;
    private static final int UINT32_NUM_BYTES = 4;
    private ArrayList<ADroneLedConfig> mLedConfigs;
    private ADroneVibrationConfig mVibrationConfig;

    static class C00581 implements Creator<ADroneNotifConfig> {
        C00581() {
        }

        public ADroneNotifConfig createFromParcel(Parcel source) {
            return new ADroneNotifConfig(null);
        }

        public ADroneNotifConfig[] newArray(int size) {
            return new ADroneNotifConfig[size];
        }
    }

    public ADroneNotifConfig(ADroneVibrationConfig vibrationConfig, ADroneLedConfig[] ledConfigs) {
        this.mLedConfigs = new ArrayList<>();
        if (vibrationConfig == null) {
            throw new NullPointerException("ADroneVibrationConfig is not allowed to be null");
        }
        this.mVibrationConfig = vibrationConfig;
        if (ledConfigs != null) {
            this.mLedConfigs.addAll(Arrays.asList(ledConfigs));
        }
    }

    private ADroneNotifConfig(Parcel source) {
        this.mLedConfigs = new ArrayList();
        readFromParcel(source);
    }


    class CONTROL_POINT_COMMAND{
        public static final int FACTORY_RESET = 255;
    }

    public static byte[] convertIntToByteArray(int value) {
        return new byte[]{(byte) (value & CONTROL_POINT_COMMAND.FACTORY_RESET), (byte) ((value >> 8) & CONTROL_POINT_COMMAND.FACTORY_RESET), (byte) ((value >> 16) & CONTROL_POINT_COMMAND.FACTORY_RESET), (byte) ((value >> 24) & CONTROL_POINT_COMMAND.FACTORY_RESET)};
    }

    public byte[] getBytes() {
        Throwable th;
        byte[] vibrationBytes = convertIntToByteArray(this.mVibrationConfig.getIntValue());
        if (this.mLedConfigs == null || this.mLedConfigs.size() <= 0) {
            return vibrationBytes;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((this.mLedConfigs.size() * UINT32_NUM_BYTES) + UINT32_NUM_BYTES);
        byte[] data = null;
        try {
            outputStream.write(vibrationBytes);
            Iterator i$ = this.mLedConfigs.iterator();
            while (i$.hasNext()) {
                outputStream.write(convertIntToByteArray(((ADroneLedConfig) i$.next()).getIntValue()));
            }
            data = outputStream.toByteArray();
            outputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return data;
    }

    public void readFromParcel(Parcel parcel) {
        this.mVibrationConfig = (ADroneVibrationConfig) parcel.readParcelable(ADroneVibrationConfig.class.getClassLoader());
        parcel.readTypedList(this.mLedConfigs, ADroneLedConfig.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mVibrationConfig, 0);
        parcel.writeTypedList(this.mLedConfigs);
    }

    static {
        CREATOR = new C00581();
    }
}

