package com.avis.app.droneAPI;


import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * MIT Licensed
 */

public class Command{
    public boolean isFloat = false;
    public boolean isInt = false;
    public boolean isString = false;

    public byte[] floatData = null;
    public byte[] intData = null;
    public byte[] stringData = null;

    public int size = 0;

    public Command(float f){
        isFloat = true;
        Log.d("aha", f + " kk");
        byte[] floatData = ByteBuffer.allocate(4).putFloat(f).array();

        try {
            String kk = Integer.toString(ByteBuffer.wrap(floatData).order(ByteOrder.LITTLE_ENDIAN).getInt());
            this.floatData = kk.getBytes("UTF-8");
            Log.d("peder", kk);
            size = this.floatData.length;
        }catch(UnsupportedEncodingException exc){
            exc.printStackTrace();
        }
    }

    public Command(int i){
        isInt = true;
        try {
            intData = Integer.toString(i).getBytes("UTF-8");
            size = intData.length;
        }catch(UnsupportedEncodingException exc){

        }
    }

    public Command(String s){
        isString = true;
        try{
            stringData = s.getBytes("UTF-8");
            size = stringData.length;
        }catch(IOException exc){

        }
    }
}