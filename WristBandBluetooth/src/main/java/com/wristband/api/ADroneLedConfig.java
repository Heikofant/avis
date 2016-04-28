package com.wristband.api;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */

public class ADroneLedConfig implements Parcelable {
    public static final Creator<ADroneLedConfig> CREATOR;
    private int mColor;
    private int mNumber;
    private int mOffDuration;
    private int mOn;
    private int mOnDuration;
    private int mRepeat;

    static class C00571 implements Parcelable.Creator<ADroneLedConfig> {
        C00571() {
        }

        public ADroneLedConfig[] newArray(int size) {
            return new ADroneLedConfig[size];
        }

        public ADroneLedConfig createFromParcel(Parcel source) {
            return new ADroneLedConfig(null);
        }
    }

    public ADroneLedConfig(boolean ledOn, boolean ledRepeat, int ledNumber, int ledOnDuration, int ledOffDuration, int color) {
        int i = 1;
        this.mOn = ledOn ? 1 : 0;
        if (!ledRepeat) {
            i = 0;
        }
        this.mRepeat = i;
        this.mNumber = ADroneVibrationConfig.validateValue(0, 4, ledNumber);
        this.mOnDuration = ADroneVibrationConfig.validateValue(0, 63, ledOnDuration);
        this.mOffDuration = ADroneVibrationConfig.validateValue(0, 63, ledOffDuration);
        this.mColor = convertRGB8888toRGB565(color);
    }

    private ADroneLedConfig(Parcel source) {
        readFromParcel(source);
    }

    public int getIntValue() {
        return (((((this.mOn << 31) | (this.mRepeat << 30)) | (this.mNumber << 28)) | (this.mOnDuration << 22)) | (this.mOffDuration << 16)) | this.mColor;
    }

    class CONTROL_POINT_COMMAND{
        public static final int FACTORY_RESET = 255;
    }

    private int convertRGB8888toRGB565(int color) {
        return (((((color >> 16) & CONTROL_POINT_COMMAND.FACTORY_RESET) >> 3) << 11) | ((((color >> 8) & CONTROL_POINT_COMMAND.FACTORY_RESET) >> 2) << 5)) | ((color & CONTROL_POINT_COMMAND.FACTORY_RESET) >> 3);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mOn);
        dest.writeInt(this.mRepeat);
        dest.writeInt(this.mNumber);
        dest.writeInt(this.mOnDuration);
        dest.writeInt(this.mOffDuration);
        dest.writeInt(this.mColor);
    }

    public void readFromParcel(Parcel parcel) {
        this.mOn = parcel.readInt();
        this.mRepeat = parcel.readInt();
        this.mNumber = parcel.readInt();
        this.mOnDuration = parcel.readInt();
        this.mOffDuration = parcel.readInt();
        this.mColor = parcel.readInt();
    }

    static {
        CREATOR = new C00571();
    }
}
