package com.example.mocom_pj;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class PassengerActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice busDevice;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private EditText messageInput;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger);

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e("kyb", "err4");
            return;
        }
        connectToBusDevice();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(messageInput.getText().toString());
            }
        });
    }

    private void connectToBusDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("Galaxy Note9")) { // 버스 장치 이름으로 변경
                    busDevice = device;
                    break;
                }
            }
        }
        if (busDevice == null) {
            Log.e("kyb", "err3");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = busDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    socket.connect();
                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    Log.e("kyb", "err1", e);
                }
            }
        }).start();
    }

    private void sendMessage(String message) {
        if (outputStream != null) {
            try {
                String fullMessage = message;
                outputStream.write(fullMessage.getBytes());
            } catch (IOException e) {
                Log.e("kyb", "err2", e);
            }
        }
    }
}