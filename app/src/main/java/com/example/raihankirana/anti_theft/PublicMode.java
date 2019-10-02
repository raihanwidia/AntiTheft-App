package com.example.raihankirana.anti_theft;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PublicMode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    CountDownTimer cdt;
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    int mSwitchSet= 0;
    ToggleButton toggle;
    AlertDialog alertDialog;
    String pin;
    TextView numberCDCharge;
    TextView textCDCharge;



    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this , accelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    protected void onPause() {
       super.onPause();
       sensorMan.unregisterListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.public_mode);super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textCDCharge = (TextView)findViewById(R.id.text_CountDPublic);
        numberCDCharge = (TextView)findViewById(R.id.count_DownPublic);
        toggle = (ToggleButton) findViewById(R.id.togglePublic);


        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        alertDialog = new AlertDialog.Builder(this).create();


        System.out.println(pin);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(MainActivity.this, "Motion Switch On", Toast.LENGTH_SHORT).show();


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
                            //info.setVisibility(View.GONE);
                            mSwitchSet = 1;
                            alertDialog.hide();
                            System.out.println(mSwitchSet);
                            numberCDCharge.setVisibility(View.INVISIBLE);
                            textCDCharge.setVisibility(View.INVISIBLE);
                            Toast.makeText(PublicMode.this, "Public Mode Switch On", Toast.LENGTH_SHORT).show();



                        }
                    }.start();


                } else {
                    Toast.makeText(PublicMode.this, "Public Mode Switch Off", Toast.LENGTH_SHORT).show();
                    mSwitchSet = 0;
                    numberCDCharge.setVisibility(View.INVISIBLE);
                    textCDCharge.setVisibility(View.INVISIBLE);
                }

            }
        });


    }

    public void onSensorChanged(SensorEvent event) {
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if (mAccel > 0.5) {
                //Toast.makeText(MainActivity.this, "Sensor Run Hua Bc", Toast.LENGTH_SHORT).show();
                //MediaPlayer mPlayer = MediaPlayer.create(MainActivity.this, R.raw.siren);
                //mPlayer.start();
                if (mSwitchSet == 1) {
//                    wakeDevice();
                    Intent intent = new Intent(this, EnterPin .class);
                    startActivity(intent);
                    finish();
                }

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
