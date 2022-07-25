package com.example.pestbotcontroller;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pestbotcontroller.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class Navigation extends FragmentActivity implements OnMapReadyCallback{


    private GoogleMap mMap;
    float zoomLevel = 19.5F;
    double latitude = 21;
    double longitude = 76;
    float heading;
    Marker marker;
    LatLng bot;
    BitmapDescriptor iconMap;
    externalThread externalThread = new externalThread();


//    InputStream inputStream = Helper.getInstance().getInputStreamer();
//    OutputStream outputStream = Helper.getInstance().getOutputStream();



//    public void sendData(byte data, byte speed) throws IOException {
////        System.out.println("Sending data");
//        outputStream.write(data);
//        outputStream.write(speed);
////        System.out.println("data sent");
//    }
//
//    public void receiveData() {
////        System.out.println("Receiving data");
//
//        latitude = 0;
//        longitude = 0;
//        heading = 0;
//        byte inputData;
//
//        try {
//            DecimalFormat df = new DecimalFormat("#.##");
//            DecimalFormat df1 = new DecimalFormat("#.#######");
//
//            inputStream.skip(inputStream.available());
//            for( int i = 0; i < 5; i++)
//            {
//                inputData = (byte) inputStream.read();
//                latitude = latitude * 100 + inputData;
//            }
//            latitude = (double) (latitude / 10000000.0);
//            System.out.println(df1.format(latitude));
//
//
//            inputStream.skip(inputStream.available());
//            for(int i = 0; i < 5; i++)
//            {
//                inputData = (byte) inputStream.read();
//                longitude = longitude * 100 + inputData;
//            }
//            longitude = (double) (longitude / 10000000.0);
////            System.out.println(df1.format(longitude));
//
//
//
//            inputStream.skip(inputStream.available());
//            for(int i = 0; i < 3; i ++)
//            {
//                inputData = (byte) inputStream.read();
//                heading = heading * 100 + inputData;
//            }
//            heading = (float) (heading / 100.0);
////            System.out.println(df.format(heading));
//
//
////            System.out.println("Data received");
////            System.out.println(latitude + " " + longitude +" " +heading);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void getGpsData()
//    {
//        try {
//            sendData((byte)9, (byte)0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        receiveData();
//
//        try {
//            sendData((byte)7, (byte)0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        Toast.makeText(getApplicationContext(), Double.toString(latitude) + "; " + Double.toString( latitude) + ", " + Float.toString(heading), Toast.LENGTH_SHORT).show();
//
//    }
//
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        System.out.println("in Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        externalThread.start();

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch satelliteSwitch = (Switch) findViewById(R.id.satelliteKey);

        ImageView setCam = findViewById(R.id.myLocation);

        setCam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bot, zoomLevel));
                return false;
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        iconMap = bitmapDescriptorFactory(getApplicationContext(), R.drawable.classic_tractor_15);

        satelliteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (satelliteSwitch.isChecked()) {
                    mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                    setCam.setImageResource(R.drawable.ic_baseline_my_location_24_white);
                }
                else {
                    mMap.setMapType(mMap.MAP_TYPE_NORMAL);
                    setCam.setImageResource(R.drawable.ic_baseline_my_location_24);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        t.cancel();
        externalThread.kill = true;
    }

    CountDownTimer t = new CountDownTimer( 1000 , 350) {

        public void onTick(long millisUntilFinished) {

            getGpsFromThread();
//            System.out.println(bot.toString());
//            Toast.makeText( getApplicationContext(), latitude + " " + longitude + " " + heading, Toast.LENGTH_SHORT).show();
            marker.setPosition(bot);
            marker.setRotation(heading);
            marker.setIcon(iconMap);

        }

        public void onFinish() {
            start();
        }
    };



    public void getGpsFromThread()
    {
        latitude = Helper.getInstance().getLatitude();
        longitude = Helper.getInstance().getLongitude();
        heading = Helper.getInstance().getHeading();
        bot = new LatLng(latitude, longitude);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
//        System.out.println(controller.latitude + " " + controller.latitude + " " + controller.heading);
//        Toast.makeText(this, latitude + " " + latitude + " " + heading, Toast.LENGTH_SHORT).show();

        while(!externalThread.ready);

        getGpsFromThread();
        MarkerOptions markerOptions= new MarkerOptions().position(bot).rotation(heading).icon(iconMap);
        marker = mMap.addMarker(markerOptions);
        assert marker != null;
        marker.setPosition(bot);
        marker.setRotation(heading);
        marker.setIcon(iconMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bot, zoomLevel));
        t.start();
    }

    private BitmapDescriptor bitmapDescriptorFactory(Context context, int vectorResId)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}

class externalThread extends Thread{

    double latitude;
    double longitude;
    float heading;
    boolean ready = false;

    boolean kill = false;

    InputStream inputStream = Helper.getInstance().getInputStream();
    OutputStream outputStream = Helper.getInstance().getOutputStream();

    public void sendData(byte data, byte speed) throws IOException {
//        System.out.println("Sending data");
        outputStream.write(data);
        outputStream.write(speed);
//        System.out.println("data sent");
    }

    public void receiveData() {
//        System.out.println("Receiving data");

        latitude = 0;
        longitude = 0;
        heading = 0;
        byte inputData;

        try {
            DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormat df1 = new DecimalFormat("#.#######");

            inputStream.skip(inputStream.available());
            for( int i = 0; i < 5; i++)
            {
                inputData = (byte) inputStream.read();
                latitude = latitude * 100 + inputData;
            }
            latitude = (double) (latitude / 10000000.0);
            System.out.println(df1.format(latitude));


            inputStream.skip(inputStream.available());
            for(int i = 0; i < 5; i++)
            {
                inputData = (byte) inputStream.read();
                longitude = longitude * 100 + inputData;
            }
            longitude = (double) (longitude / 10000000.0);
//            System.out.println(df1.format(longitude));



            inputStream.skip(inputStream.available());
            for(int i = 0; i < 3; i ++)
            {
                inputData = (byte) inputStream.read();
                heading = heading * 100 + inputData;
            }
            heading = (float) (heading / 100.0);
//            System.out.println(df.format(heading));


//            System.out.println("Data received");
//            System.out.println(latitude + " " + longitude +" " +heading);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getGpsData()
    {
        try {
            sendData((byte)9, (byte)0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveData();

        try {
            sendData((byte)7, (byte)0);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Toast.makeText(getApplicationContext(), Double.toString(latitude) + "; " + Double.toString( latitude) + ", " + Float.toString(heading), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void run(){

        while (true)
        {
            getGpsData();
            Helper.getInstance().setLatitude(latitude);
            Helper.getInstance().setLongitude(longitude);
            Helper.getInstance().setHeading(heading);

            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ready = true;

            if(kill)
                break;
        }

    }
}