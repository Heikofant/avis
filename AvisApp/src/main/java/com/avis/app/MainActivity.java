package com.avis.app;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import com.avis.app.droneAPI.Command;
import com.wristband.api.BandMode;
import com.wristband.api.BtAction;
import com.wristband.api.BtBand;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;

/**
 * Copyright (c) 2016 Avis for Space Apps
 * MIT Licensed
 */

public class MainActivity extends AppCompatActivity implements BandListener.BandHandler, LocListener.LocationHandler,
        YahooWeatherInfoListener{

    private TextView textStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStatus = (TextView) findViewById(R.id.text_status);
        findViewById(R.id.button_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });

        band = new BtBand(this);
        band.addListener(new BandListener(this));
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocListener(this);
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, listener);
        }catch(SecurityException exc){
            exc.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    sendCmd("COMWDG", new Command[]{});
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException exc){

                    }
                }
            }
        }).start();
    }

    WeatherInfo curInfo = null;

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo) {
        curInfo = weatherInfo;
    }

    int sequence = 1;

    void takeOff(){
        sendCmd("FTRIM", new Command[]{});
        sendCmd("CONFIG", new Command[]{new Command("control:altitude_max"), new Command(30000)});
        ref(true, false);
    }

    void ref(boolean takeOff, boolean emergency){
        int p = 290717696;
        if(takeOff) {
            p += 512;
        }
        if(emergency){
            p += 256;
        }
        sendCmd("REF", new Command[]{new Command(p)});
    }

    void sendCmd(String name, Command[] cmds){
        InetAddress add = null;
        try{
            add = InetAddress.getByName("192.168.1.1");
        }catch(UnknownHostException exc){
            exc.printStackTrace();
        }

        String sequenceStr = Integer.toString(sequence, 10);

        try {
            DatagramSocket sock = new DatagramSocket();
            byte[] command = name.getBytes("UTF-8");
            byte[] seq = sequenceStr.getBytes("UTF-8");

            int size = 0;
            for(int i = 0; i < cmds.length; i++){
                size+= cmds[i].size + 1;
            }

            ByteBuffer buff = ByteBuffer.allocate(size);
            for(int i = 0; i < cmds.length; i++){
                Command cmd = cmds[i];
                buff.put(",".getBytes("UTF-8"));
                if(cmd.isString){
                    buff.put(cmd.stringData);
                }else if(cmd.isFloat){
                    buff.put(cmd.floatData);
                }else if(cmd.isInt){
                    buff.put(cmd.intData);
                }
            }

            byte[] param = buff.array();
            Log.d("filipco", new String(param, Charset.defaultCharset()));

            ByteBuffer buf = ByteBuffer.allocate(3 + command.length + 1 + seq.length + param.length + 1);
            buf.put("AT*".getBytes("UTF-8"));
            buf.put(command);
            buf.put("=".getBytes("UTF-8"));
            buf.put(seq);
            buf.put(param);
            buf.put("\r".getBytes("UTF-8"));

            byte[] data = buf.array();
            if(!command.equals("COMWDG"))
            Log.d("filipco", new String(data, Charset.defaultCharset()));
            sock.send(new DatagramPacket(data, data.length, add, 5556));
        }catch(SocketException exc){
            exc.printStackTrace();
        }catch(IOException exc){
            exc.printStackTrace();
        }
        sequence+= 1;
    }

    private BtBand band;

    private void connect(){
        textStatus.setText("connecting...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    try {
                        band.connect();
                        Thread.sleep(1000);
                    }catch(InterruptedException exc){
                        exc.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    boolean tookOff = false;
    boolean land = false;
    @Override
    public void onButtonClick() {
        if(tookOff){
            if(!land) {
                textStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        textStatus.setText("land");
                    }
                });
                ref(false, false);
                land = true;
            }else{
                ref(false, true);
            }
            //hudHandler.emergency();
        }else{
            takeOff();
            textStatus.post(new Runnable() {
                @Override
                public void run() {
                    textStatus.setText("take off");
                }
            });
            tookOff = true;
            //hudHandler.takeOff();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(2000);
                        band.addRequest(BtAction.Action.ENABLE_ACC_NOTIFICATIONS);
                        Thread.sleep(500);
                        for(int i = 0; i < 3; i++){
                            band.addRequest(BtAction.Action.SET_MODE, BandMode.getInt(BandMode.AccessoryMode.MEDIA));
                            Thread.sleep(500);
                            band.addRequest(BtAction.Action.REQUEST_MODE);
                            Thread.sleep(500);
                            band.addRequest(BtAction.Action.ENABLE_ACC_NOTIFICATIONS);
                            Thread.sleep(500);
                        }
                    }catch (InterruptedException exc){
                        exc.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onAccData(float x, float y, float z) {
        x/= 9.81;
        y/= 9.81;
        z/= 9.81;

        double pitch = Math.atan(y/Math.hypot(x, z));
        float pitchDeg = (float) Math.toDegrees(pitch);

        double roll = Math.atan(-x/z);
        float rollDeg = (float) Math.toDegrees(roll);

        sendCmd("PCMD", new Command[]{new Command(1), new Command(rollDeg/130), new Command(pitchDeg/130), new Command(0.0f), new Command(0.0f)});
    }

    @Override
    public void onBandConnection(boolean connected) {
        if(connected){
            textStatus.post(new Runnable() {
                @Override
                public void run() {
                    textStatus.setText("connected");
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        band.addRequest(BtAction.Action.ENABLE_EVENT_NOTIFICATIONS);
                        Thread.sleep(500);
                        for (int i = 0; i < 3; i++) {
                            band.addRequest(BtAction.Action.SET_MODE, BandMode.getInt(BandMode.AccessoryMode.DAY));
                            Thread.sleep(300);
                            band.addRequest(BtAction.Action.REQUEST_MODE);
                            Thread.sleep(300);
                        }
                    }catch(InterruptedException exc){
                        exc.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onLocation(double long_, double lat_) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        band.disconnect();
    }
}
