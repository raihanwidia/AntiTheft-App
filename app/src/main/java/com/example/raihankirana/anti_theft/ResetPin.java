package com.example.raihankirana.anti_theft;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class ResetPin extends AppCompatActivity {
    EditText etOldPin,etSetPin,etConfirmPin;
    Button btnSetPin;
    String url = "https://112.137.167.34/~thisisnt/AntiTheft";
    String username , regisPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pin);

        etOldPin = (EditText)findViewById(R.id.etOldPin);
        etSetPin = (EditText)findViewById(R.id.etSetPin);
        etConfirmPin = (EditText)findViewById(R.id.etConfirmPin);
        btnSetPin = (Button)findViewById(R.id.btnSetPin);


        SharedPreferences settings = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        username = settings.getString("USERNAME", null);
        regisPin = settings.getString("PASSWORD", null);

        System.out.println(regisPin);

        btnSetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPin = etOldPin.getText().toString();
                String pin = etSetPin.getText().toString();
                String confirmPin = etConfirmPin.getText().toString()  ;

                System.out.println(oldPin);
                System.out.println(pin);
                System.out.println(confirmPin);

                if(oldPin.equals(regisPin)){
                    if (pin.equals(confirmPin)){
                        ResetPin(username,pin,oldPin);
                        SharedPreferences settings = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove("PASSWORD");
                        editor.commit();
                        editor.putString("PASSWORD", pin);
                        editor.commit();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Pin", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void ResetPin(final String username, final String password, final String oldpassword) {
        class resetpin extends AsyncTask<Void,Void,String> {

            ProgressDialog pd;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(ResetPin.this);
                pd.setMessage("Reseting Pin");
                pd.show();
            }


            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username",username);
                hashMap.put("password",password);
                hashMap.put("oldpassword",oldpassword);

                com.example.raihankirana.anti_theft.RequestHandler rh = new com.example.raihankirana.anti_theft.RequestHandler();
                String s = rh.sendPostRequest(url+"/resetpin.php",hashMap);
                System.out.println(s);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("pin has been changed")){
                    Toast.makeText(ResetPin.this, "Pin has been changed",
                            Toast.LENGTH_LONG).show();
                    if (pd != null)
                    {
                        pd.dismiss();
                    }
                    Intent intent = new Intent(ResetPin.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ResetPin.this, "Wrong Pin",
                            Toast.LENGTH_LONG).show();
                    if (pd != null)
                    {
                        pd.dismiss();
                    }
                }
            }
        }
        try {
            resetpin luser = new resetpin();
            luser.execute();
        }catch(Exception e){}

    }

}
