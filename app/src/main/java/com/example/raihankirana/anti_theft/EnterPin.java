package com.example.raihankirana.anti_theft;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


public class EnterPin extends AppCompatActivity {
    EditText etEnterPin;
    SharedPreferences sharedpreferences;
    View view;
    String password;
    MediaPlayer mPlayer;
    Intent intentbacktomain;


    //Disable Back Key
    @Override
    public void onBackPressed() {
        //return nothing
        return;
    }

    //Disable Tasks Key
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    //Disable Volume Key
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // Do your thing

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pin);

        /*final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000};
        vb.vibrate(pattern, 0);*/
        SharedPreferences settings = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        password = settings.getString("PASSWORD",null);

        intentbacktomain = new Intent(this, MainActivity .class);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        mPlayer = MediaPlayer.create(EnterPin.this, R.raw.siren);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mPlayer.start();
        mPlayer.setLooping(true);
        etEnterPin = (EditText) findViewById(R.id.etEnterPin);
        etEnterPin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String pin = etEnterPin.getText().toString();
                    if (pin.equals(password)) {
                        System.out.println(pin);
                        mPlayer.stop();
                        //vb.cancel();lost

                        intentbacktomain.putExtra("Pin" ,pin);
                        startActivity(intentbacktomain);
                        finish();
                        handled = true;
                    } else {
                        etEnterPin.getText().clear();
                        etEnterPin.setError("Wrong Pin !");
                        etEnterPin.requestFocus();


                    }

                }
                return handled;
            }
        });

    }
}
