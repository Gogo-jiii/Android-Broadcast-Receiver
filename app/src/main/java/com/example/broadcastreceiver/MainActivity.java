package com.example.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtResult;
    private IntentFilter implicitIntentFilter, localBroadcastIntentFilter;
    private Button btnSendLocalBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.txtResult);
        btnSendLocalBroadcast = findViewById(R.id.btnSendLocalBroadcast);

        implicitIntentFilter = new IntentFilter();
        implicitIntentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        localBroadcastIntentFilter = new IntentFilter();
        localBroadcastIntentFilter.addAction("local_broadcast");

        btnSendLocalBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent("local_broadcast");
                intent.putExtra("key", "IT wala");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
            }
        });

    }

    @Override protected void onResume() {
        super.onResume();
        registerReceiver(implicitBroadCastReceiver, implicitIntentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadCastReceiver,
                localBroadcastIntentFilter);
    }

    @Override protected void onStop() {
        unregisterReceiver(implicitBroadCastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadCastReceiver);
        super.onStop();
    }

    BroadcastReceiver implicitBroadCastReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d("TAG", log);
            Toast.makeText(context, log, Toast.LENGTH_LONG).show();
            txtResult.setText(log);
        }
    };

    BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("key");
            Log.d("TAG", name);
            Toast.makeText(context, name, Toast.LENGTH_LONG).show();
            txtResult.setText(name);
        }
    };
}