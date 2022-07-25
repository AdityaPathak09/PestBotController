package com.example.pestbotcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {


    private static MainActivity mainActivity;
    String name = "";
    String addressToConnect = "";
    BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;

    public static MainActivity getInstance(){
        if(mainActivity != null)
            return mainActivity;
        return new MainActivity();
    }


    @SuppressLint("MissingPermission")
    public void connect(BluetoothDevice myDevice)
    {

        final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        int counter = 0;
        do {
            try {
//                            System.out.println(myDevice.toString());
                bluetoothSocket = myDevice.createRfcommSocketToServiceRecord(mUUID);
//                            System.out.println(bluetoothSocket);
                bluetoothSocket.connect();
//                System.out.println(bluetoothSocket.isConnected());
                if(bluetoothSocket.isConnected())
                    Toast.makeText(this, "Connected to " +myDevice.getName(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Cannot connect to " +myDevice.getName() +". Please retry.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            counter++;
        }
        while(!bluetoothSocket.isConnected() && counter < 5);


        try {
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            Helper.getInstance().setInputStreamer(inputStream);
            Helper.getInstance().setOutputStreamer(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView textView = findViewById(R.id.address);
        Button button = findViewById(R.id.enterAddressButton);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();

        Toast.makeText(this, "Connecting to " +name, Toast.LENGTH_SHORT).show();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothDevice myDevice = null;
                name = textView.getText().toString();
//                textView.setText("");

                if (pairedDevices.size() > 0) {
                    // Loop through paired device
                    for (BluetoothDevice device : pairedDevices) {
//                        Toast.makeText(getApplicationContext(), device.getName().toString(), Toast.LENGTH_SHORT).show();

                        if (device.getName().toString().equals(name)) {
                            addressToConnect = bluetoothAdapter.getRemoteDevice(device.getAddress()).toString();
                            Toast.makeText(getApplicationContext(),device.getName().toString() + ": "+ addressToConnect, Toast.LENGTH_LONG).show();
                            myDevice = bluetoothAdapter.getRemoteDevice(addressToConnect);

                            Helper.getInstance().setBluetoothDevice(myDevice);
                            Toast.makeText(getApplicationContext(), myDevice.toString(), Toast.LENGTH_SHORT).show();
//                            System.out.println(myDevice.getName().toString());
                            Toast.makeText(getApplicationContext(), bluetoothAdapter.getRemoteDevice(device.getAddress()).toString(), Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                    connect(myDevice);
                    Intent intent = new Intent(getApplicationContext(), Controller.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "no devices connected", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

}