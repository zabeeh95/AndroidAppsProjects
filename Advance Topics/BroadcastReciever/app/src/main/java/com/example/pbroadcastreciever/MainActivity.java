package com.example.pbroadcastreciever;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The Power Receiver app responds to system broadcasts about the power
 * connected state as well as a custom broadcast that is sent when the user
 * taps the button.
 */
public class MainActivity extends AppCompatActivity {

    private CustomReceiver mReceiver = new CustomReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define the IntentFilter.
        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mReceiver,filter);

        IntentFilter IF = new IntentFilter("com.example.pbroadcastreciever");
        registerReceiver(mReceiver,IF);

 }

    /**
     * Click event handler for the button, that sends custom broadcast using the
     * LocalBroadcastManager.
     */
    public void sendCustomBroadcast(View view) {
        Intent intent = new Intent("com.example.pbroadcastreciever");
        intent.putExtra("main_activity","broadcast recieved");
        sendBroadcast(intent);

    }

    /**
     * Unregisters the broadcast receivers when the app is destroyed.
     */
    @Override
    protected void onDestroy() {
        // Unregister the receivers.
        this.unregisterReceiver(mReceiver);
        super.onDestroy();
    }


}