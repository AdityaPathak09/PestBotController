package com.example.pestbotcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Controller extends AppCompatActivity{

    String addressToConnect = "";
    byte speed = 0;
    byte camAngle = 90;
    byte backEdge = 0;
    byte backCentre = 0;
    byte mode = 7;
    byte inputData ;
    BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream = Helper.getInstance().getOutputStream();
    InputStream inputStream = Helper.getInstance().getInputStream();

    String camSsid = "pestBotCam";
    String camPass = "pestBotCamPassword123";

    String url = "http://192.168.186.1/mjpeg/1";

//    String url = "https://Instagram.com";



    public void sendData(byte data, byte speed) throws IOException {
//        System.out.println("Sending data");
        outputStream.write(data);
        outputStream.write(speed);
//        System.out.println("data sent");
    }



//    @SuppressLint("MissingPermission")
//    public void connect(BluetoothDevice myDevice)
//    {
//
//        final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//        int counter = 0;
//        do {
//            try {
////                            System.out.println(myDevice.toString());
//                bluetoothSocket = myDevice.createRfcommSocketToServiceRecord(mUUID);
////                            System.out.println(bluetoothSocket);
//                bluetoothSocket.connect();
////                System.out.println(bluetoothSocket.isConnected());
//                if(bluetoothSocket.isConnected())
//                    Toast.makeText(this, "Connected to " +myDevice.getName(), Toast.LENGTH_SHORT).show();
//            } catch (IOException e) {
//                Toast.makeText(this, "Cannot connect to " +myDevice.getName() +". Please retry.", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//            counter++;
//        }
//        while(!bluetoothSocket.isConnected() && counter < 5);
//
//
//        try {
//            outputStream = bluetoothSocket.getOutputStream();
//            inputStream = bluetoothSocket.getInputStream();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }


    @SuppressLint({"MissingPermission", "SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        Bundle extras = getIntent().getExtras();
//
//        divName= extras.getString("name");


        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", camSsid);
        wifiConfig.preSharedKey = String.format("\"%s\"", camPass);
        wifiConfig.hiddenSSID = true;

        System.out.println("SSID: " +wifiConfig.SSID.toString());
        System.out.println("Password: " +wifiConfig.preSharedKey.toString());

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        System.out.println("WiFi connection Info: " + wifiManager.getConnectionInfo());

//        wifiManager.disconnect();
//        int netId = wifiManager.addNetwork(wifiConfig);

//        while(!wifiManager.enableNetwork(netId, true));
//        wifiManager.enableNetwork(netId, true);
//        wifiManager.reconnect();
//        System.out.println("WiFi connection Info: " + wifiManager.getConnectionInfo());


//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        bluetoothAdapter.startDiscovery();
//
//        Toast.makeText(this, "Connecting to " +divName, Toast.LENGTH_SHORT).show();
//
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//
//        BluetoothDevice myDevice = null;

//        if (pairedDevices.size() > 0) {
//            // Loop through paired device
//            for (BluetoothDevice device : pairedDevices) {
////                        Toast.makeText(getApplicationContext(), device.getName().toString(), Toast.LENGTH_SHORT).show();
//
//                if (device.getName().toString().equals(divName)) {
//                    addressToConnect = bluetoothAdapter.getRemoteDevice(device.getAddress()).toString();
////                            Toast.makeText(getApplicationContext(),device.getName().toString() + ": "+ addressToConnect, Toast.LENGTH_LONG).show();
//                    myDevice = bluetoothAdapter.getRemoteDevice(addressToConnect);
////                             Toast.makeText(this, myDevice, Toast.LENGTH_SHORT).show();
////                            System.out.println(myDevice.getName().toString());
//                    break;
////                          Toast.makeText(getApplicationContext(), bluetoothAdapter.getRemoteDevice(device.getAddress()).toString(), Toast.LENGTH_LONG).show();
//                }
//            }
        //            connect(myDevice);
//        }
//        else {
//
//            Toast.makeText(this, "No device connected", Toast.LENGTH_SHORT).show();
//        }



            ImageView forward = findViewById(R.id.viewforward);
            ImageView backward = findViewById(R.id.viewback);
            ImageView right = findViewById(R.id.viewright);
            ImageView left = findViewById(R.id.viewleft);
            ImageView sright = findViewById(R.id.viewsr);
            ImageView sleft = findViewById(R.id.viewsl);
            ImageView stop = findViewById(R.id.viewstop);
            Button mapButton = findViewById(R.id.mapButton);
            SeekBar speedbar = findViewById(R.id.speedBar);
            SeekBar camSeek = findViewById(R.id.camAngle);
            SwitchCompat pump1 = findViewById(R.id.pump1Button);
            SwitchCompat pump2 = findViewById(R.id.pump2Button);

            SeekBar sprayerEHeight = findViewById(R.id.backEdge);
            SeekBar sprayerCHeight = findViewById(R.id.backCentre);

            Button captImag = findViewById(R.id.captImgButton);

            captImag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sendData((byte)14, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            final ImageView[] lastMove = new ImageView[1];

            sprayerCHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBar.setMax(127);
                    backCentre = (byte) progress;
                    try {
                        sendData((byte)12, (byte)backCentre);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
//                    try {
//                        sendData((byte)12, (byte)backCentre);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        sendData((byte)mode, (byte)speed);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    try {
                        sendData((byte)12, (byte)backCentre);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            sprayerEHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBar.setMax(127);
                    backEdge = (byte) progress;
                    try {
                        sendData((byte)13, (byte)backEdge);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
//                    try {
//                        sendData((byte)13, (byte)backEdge);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        sendData((byte)mode, (byte)speed);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    try {
                        sendData((byte)13, (byte)backEdge);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });





            WebView webView = findViewById(R.id.webView);

            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setInitialScale(1);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);
            System.out.println("Wifi State: " +wifiManager.getWifiState());
            webView.loadUrl(url);


            pump1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (pump1.isChecked()) {
                        try {
                            sendData((byte)10, (byte)1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            sendData((byte)10, (byte)0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

            pump2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (pump2.isChecked()) {
                        try {
                            sendData((byte)11, (byte)1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            sendData((byte)11, (byte)0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Helper.getInstance().setInputStreamer(inputStream);
//                    Helper.getInstance().setOutputStreamer(outputStream);
                    Intent intent = new Intent(getApplicationContext(), Navigation.class);
//                    System.out.println("Starting Activity");
                    startActivity(intent);
                }
            });


            camSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBar.setMax(180);
                    progress = 180 - progress;
                    camAngle = (byte)progress;
                    try {
                        sendData((byte)8, (byte)camAngle);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                    try {
                        sendData((byte)8, (byte)camAngle);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    try {
                        sendData((byte)8, (byte)camAngle);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            camSeek.setProgress(90);

            Button reconnect = findViewById(R.id.reconnectButton);

            BluetoothDevice finalMyDevice = Helper.getInstance().getBluetoothDevice();
            reconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.getInstance().connect(finalMyDevice);
                }
            });

            speedbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBar.setMax(255);
                    speed = (byte)progress;
//                    Toast.makeText(getApplicationContext(), "Speed: "+progress, Toast.LENGTH_SHORT).show();
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    try {
                        sendData((byte)mode, (byte)speed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            forward.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode = 1;
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sendData((byte) mode, (byte) speed);
                            forward.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                            Helper.getInstance().setLastMove(forward);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP )
                    {
                        try {
                            sendData((byte) 7, (byte) speed);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }

            });

            backward.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            sendData((byte)7, (byte) speed);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sendData((byte)mode, (byte) speed);
                            backward.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                            Helper.getInstance().setLastMove(backward);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return false;
                }
            });

            left.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode = 5;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            sendData((byte)7, (byte) speed);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sendData((byte)mode, (byte) speed);
                            left.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                            Helper.getInstance().setLastMove(left);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return false;
                }
            });

            right.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode  = 6;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            sendData((byte)7, (byte) speed);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sendData((byte)mode, (byte) speed);
                            right.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                            Helper.getInstance().setLastMove(right);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return false;
                }
            });

            sleft.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode = 3;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            sendData((byte)7, (byte) speed);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sendData((byte)mode, (byte) speed);
                            sleft.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                            Helper.getInstance().setLastMove(sleft);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return false;
                }
            });

            sright.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode = 4;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            sendData((byte)7, (byte) speed);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sendData((byte)mode, (byte) speed);
                            sright.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                            Helper.getInstance().setLastMove(sright);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return false;
                }
            });

            stop.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mode = 7;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        try {
                            sendData((byte)7, (byte) speed);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            speedbar.setProgress(0);
                            sendData((byte)mode, (byte) speed);
                            Helper.getInstance().setLastMove(stop);
                            stop.setBackgroundColor(R.color.purple_500);
                            Helper.getInstance().getLastMove().setBackgroundColor(R.color.transperant);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return false;
                }
            });


    }
}