package com.example.pestbotcontroller;

import android.bluetooth.BluetoothDevice;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.OutputStream;

import kotlin.collections.LongIterator;

public class Helper {

    private static Helper helper;
    private static InputStream inputStream;
    public static OutputStream outputStream;
    public static double latitude;
    public static double longitude;
    public static float heading;
    public static ImageView lastMove;
    public static BluetoothDevice bluetoothDevice;

    private Helper(){

    }

    public static Helper getInstance(){
        if(helper != null)
            return helper;
        return new Helper();
    }

    public void setInputStreamer(InputStream is){
        inputStream = is;
    }
    public void setOutputStreamer(OutputStream os){
       outputStream = os;
    }
    public void setLatitude(double lat){latitude = lat; }
    public void setLongitude(double lon){longitude = lon; }
    public void setHeading(float head){heading = head; }
    public void setLastMove(ImageView lastmove){lastMove = lastmove; }
    public void setBluetoothDevice(BluetoothDevice btdevice){bluetoothDevice = btdevice;}

    public InputStream getInputStream(){
        return inputStream;
    }
    public OutputStream getOutputStream(){
        return outputStream;
    }
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public float getHeading(){return heading;}
    public ImageView getLastMove(){return lastMove;}
    public BluetoothDevice getBluetoothDevice(){return bluetoothDevice;}

}
