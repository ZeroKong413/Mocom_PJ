package com.example.mocom_pj;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DriverActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket serverSocket;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView receivedMessages;
    private LinearLayout messagesLayout;
    private Set<String> blockedDevices = new HashSet<>();
    private Map<String, Button> blockButtons = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver);

//        receivedMessages = findViewById(R.id.receivedMessages);
        messagesLayout = findViewById(R.id.messagesLayout);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e("kyb", "err8");
            return;
        }
        startServer();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateMessages();
                handler.postDelayed(this, 3000); // 3초마다 업데이트
            }
        }, 3000);


    }
    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BusApp", UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    BluetoothSocket socket;
                    while ((socket = serverSocket.accept()) != null) {
                        handleClientSocket(socket);
                    }
                } catch (IOException e) {
                    Log.e("kyb", "err9", e);
                }
            }
        }).start();
    }

    private void handleClientSocket(BluetoothSocket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytes;
                    String deviceName = socket.getRemoteDevice().getName();

                    while ((bytes = inputStream.read(buffer)) != -1) {
                        final String message = deviceName + ": " + new String(buffer, 0, bytes);
                        if (!blockedDevices.contains(deviceName)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView textView = new TextView(DriverActivity.this);
                                    textView.setText(message);
                                    messagesLayout.addView(textView);
                                    addBlockButton(deviceName);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    Log.e("kyb", "Error handling client socket", e);
                }
            }
        }).start();
    }
    private void addBlockButton(final String deviceName) {
        if (!blockButtons.containsKey(deviceName)) {
            Button blockButton = new Button(this);
            blockButton.setText("Block " + deviceName);
            blockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockedDevices.add(deviceName);
                }
            });
            blockButtons.put(deviceName, blockButton);
            messagesLayout.addView(blockButton);
        }
    }
    private void updateMessages() {
        // 메시지를 가져와서 화면을 업데이트하는 로직
    }

}