package com.example.raihankirana.anti_theft;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Register extends AppCompatActivity {


    EditText SetPin , ConfirmPin , usernamereg , Email;
    Button btnRegister;
    CheckBox rememberme;
    TextView forgotpin;
    String url = "https://112.137.167.34/~thisisnt/AntiTheft";
    String pin;
    String confirmPin;
    String email;
    String username;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        SetPin = (EditText) findViewById(R.id.PINreg);
        ConfirmPin = (EditText)findViewById(R.id.PINreg2);
        btnRegister = (Button)findViewById(R.id.reg);
        Email = (EditText)findViewById(R.id.emailReg);
        usernamereg = (EditText)findViewById(R.id.usernameReg);

//
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin = SetPin.getText().toString();
                confirmPin = ConfirmPin.getText().toString();
                email = Email.getText().toString();
                username = usernamereg.getText().toString();


                if(TextUtils.isEmpty(email)) {
                    Email.setError("Required");
                    Email.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(username)) {
                    usernamereg.setError("Required");
                    usernamereg.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(pin) || pin.length()<4) {
                    SetPin.setError("Required! Minimum length 4 digit");
                    SetPin.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(confirmPin) || confirmPin.length()<4) {
                    ConfirmPin.setError("Required! Minimum length 4 digit");
                    ConfirmPin.requestFocus();
                    return;
                }

                if(pin.equals(confirmPin)){
                    registerUser(username,email,confirmPin);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password Does Not Match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerUser(final String username, final String Email, final String password ) {
        class RegisterUser extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username",username);
                hashMap.put("email",Email);
                hashMap.put("password",password);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(url+"/register.php",hashMap);
                System.out.println(s);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("1")){
                    Toast.makeText(Register.this, "Your username is already registered",
                            Toast.LENGTH_LONG).show();

                }else if(s.equals("2")){
                    Toast.makeText(Register.this, "Regitered",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Register.this, "Your username is already registered",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            RegisterUser  ruser = new RegisterUser ();
            ruser.execute();
        }catch(Exception e){}
    }

}
