package com.example.raihankirana.anti_theft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class chargerMode extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorManager sensorMan;
    private Sensor accelerometer;
    AlertDialog alertDialog;
    int chargerFlag, chargerFlag1, chargerFlag2 = 0;
    String pin;
    ToggleButton toggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charger_mode);
        System.out.println(pin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = (ToggleButton) findViewById(R.id.toogleCharge);

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    chargerFlag = 1;
                } else if (plugged == 0) {
                    chargerFlag1 = 1;
                    chargerFlag = 0;
                    func();

                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    if (chargerFlag != 1) {
                        Toast.makeText(chargerMode.this, "Connect To Charger First !", Toast.LENGTH_SHORT).show();
                        toggle.setChecked(false);
                    } else {
                        Toast.makeText(chargerMode.this, "Charger Protection Mode On", Toast.LENGTH_SHORT).show();
                        chargerFlag2 = 1;
                        func();
                    }
                }else{
                    chargerFlag2 = 0;
                }
            }
        });

    }

    public void func() {
      //  Toast.makeText(chargerMode.this, "ChargerFlag" + chargerFlag + "Chargerflag1:" + chargerFlag1, Toast.LENGTH_SHORT).show();
        if (chargerFlag == 0 && chargerFlag1 == 1 && chargerFlag2 == 1) {
            Intent intent = new Intent(this, EnterPin .class);
            startActivity(intent);
            chargerFlag2 = 0;
            finish();
        }
    }


}
