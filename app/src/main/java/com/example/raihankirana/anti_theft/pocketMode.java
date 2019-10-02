package com.example.raihankirana.anti_theft;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class pocketMode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static final int SENSOR_SENSITIVITY = 4;
    int pSwitchSet = 0;
    ToggleButton toggle;
    CountDownTimer cdt;
    String pin;
    TextView numberCDCharge;
    TextView textCDCharge;
    PowerManager pm;



    public void onResume() {
        super.onResume();
        mSensorManager.registerListener( this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textCDCharge = (TextView)findViewById(R.id.text_CountDPocket);
        numberCDCharge = (TextView)findViewById(R.id.count_DownPocket);
        toggle = (ToggleButton) findViewById(R.id.tooglePocket);


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    cdt = new CountDownTimer(10000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            int num = (int) (millisUntilFinished / 1000);
                            numberCDCharge.setText (""+num);
                            numberCDCharge.setVisibility(View.VISIBLE);
                            textCDCharge.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinish() {
                            pSwitchSet = 1;
                            numberCDCharge.setVisibility(View.INVISIBLE);
                            textCDCharge.setVisibility(View.INVISIBLE);
                            Toast.makeText(pocketMode.this, "Pocket Mode Activated", Toast.LENGTH_SHORT).show();

                        }
                    }.start();



                } else {
                    Toast.makeText(pocketMode.this, "Pocket Mode Off", Toast.LENGTH_SHORT).show();
                    pSwitchSet = 0;
                    numberCDCharge.setVisibility(View.INVISIBLE);
                    textCDCharge.setVisibility(View.INVISIBLE);
                }
            }
        });


    }


    public void onSensorChanged(SensorEvent event) {

                if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                    //near
//                    Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
                } else if (pSwitchSet == 1) {
                    Intent intent = new Intent(this, EnterPin .class);
                    startActivity(intent);
                    finish();
                    //far
//                    Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();

                }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
