package com.example.raihankirana.anti_theft;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class ForgotPin extends AppCompatActivity {

    EditText username;
    Button btnSendPin;
    String url = "https://112.137.167.34/~thisisnt/AntiTheft";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpin);
        username = (EditText) findViewById(R.id.usernameForg);
        btnSendPin = (Button) findViewById(R.id.sendPin);
        btnSendPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(username.getText().toString());
            }
        });

    }
    private void sendEmail(final String username) {
        class LoginUser extends AsyncTask<Void,Void,String> {

            ProgressDialog pd;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(ForgotPin.this);
                pd.setMessage("Sending Your Pin to your Email");
                pd.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username",username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(url+"/forgotpin.php",hashMap);
                System.out.println(s);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                    Toast.makeText(ForgotPin.this,  s,
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPin.this , Login.class);
                    startActivity(intent);
                if (pd != null)
                {
                    pd.dismiss();
                }

            }
        }
        try {
            LoginUser luser = new LoginUser();
            luser.execute();
        }catch(Exception e){}
    }




}
