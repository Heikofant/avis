package com.wristband.api;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * This code may contain MIT licensed code from https://github.com/fbarriga
 */

public class ADroneVibrationConfig implements Parcelable {
    public static final Creator<ADroneVibrationConfig> CREATOR;
    private int mOffDuration;
    private int mOn;
    private int mOnDuration;
    private int mRepeat;
    private int mRepeatCount;

    static class C00591 implements Creator<ADroneVibrationConfig> {
        C00591() {
        }

        public ADroneVibrationConfig[] newArray(int size) {
            return new ADroneVibrationConfig[size];
        }

        public ADroneVibrationConfig createFromParcel(Parcel source) {
            return new ADroneVibrationConfig(source);
        }
    }

    public ADroneVibrationConfig(boolean vibOn, boolean vibRepeat, int vibRepeatCount, int vibOnDuration, int vibOffDuration) {
        int i = 1;
        this.mOn = vibOn ? 1 : 0;
        if (!vibRepeat) {
            i = 0;
        }
        this.mRepeat = i;
        this.mRepeatCount = validateValue(0, 63, vibRepeatCount);
        this.mOnDuration = validateValue(0, 4095, vibOnDuration);
        this.mOffDuration = validateValue(0, 4095, vibOffDuration);
    }

    public static int validateValue(int min, int max, int value) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public ADroneVibrationConfig(Parcel source) {
        readFromParcel(source);
    }

    public int getIntValue() {
        return ((((this.mOn << 31) | (this.mRepeat << 30)) | (this.mRepeatCount << 24)) | (this.mOnDuration << 12)) | this.mOffDuration;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mOn);
        dest.writeInt(this.mRepeat);
        dest.writeInt(this.mRepeatCount);
        dest.writeInt(this.mOnDuration);
        dest.writeInt(this.mOffDuration);
    }

    public void readFromParcel(Parcel parcel) {
        this.mOn = parcel.readInt();
        this.mRepeat = parcel.readInt();
        this.mRepeatCount = parcel.readInt();
        this.mOnDuration = parcel.readInt();
        this.mOffDuration = parcel.readInt();
    }

    static {
        CREATOR = new C00591();
    }
}
