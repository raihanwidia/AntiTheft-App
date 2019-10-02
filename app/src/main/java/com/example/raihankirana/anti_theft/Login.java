package com.example.raihankirana.anti_theft;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {


    EditText usernamelog;
    EditText pinlog;
    Button login , registerlog;
    CheckBox rememberme;
    TextView forgotpin;
    private Boolean saveLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    String url = "https://112.137.167.34/~thisisnt/AntiTheft";
    String username , pin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernamelog = (EditText) findViewById(R.id.usernameLogin);
        pinlog = (EditText) findViewById(R.id.PINLogin);
        rememberme = findViewById(R.id.rememberMe);
        login = findViewById(R.id.login);
        registerlog = findViewById(R.id.register);
        forgotpin = findViewById(R.id.forgotPin);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            usernamelog.setText(loginPreferences.getString("username", ""));
            pinlog.setText(loginPreferences.getString("password", ""));
            rememberme.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = pinlog.getText().toString();
                String username = usernamelog.getText().toString();
                loginUser(username,pin);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(usernamelog.getWindowToken(), 0);

                username = usernamelog.getText().toString();
                pin = pinlog.getText().toString();

                if (rememberme.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", pin);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

            }
        });

        registerlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        forgotpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPin.class);
                startActivity(intent);
            }
        });



    }

    private void loginUser(final String username, final String password) {
        class LoginUser extends AsyncTask<Void,Void,String> {

            ProgressDialog pd;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(Login.this);
                pd.setMessage("Logging in");
                pd.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username",username);
                hashMap.put("password",password);

                com.example.raihankirana.anti_theft.RequestHandler rh = new com.example.raihankirana.anti_theft.RequestHandler();
                String s = rh.sendPostRequest(url+"/login.php",hashMap);
                System.out.println(s);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(Login.this, "Login Success",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    SharedPreferences settings = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("USERNAME", username);
                    editor.putString("PASSWORD", password);
                    editor.commit();
                    startActivity(intent);
                    if (pd != null)
                    {
                        pd.dismiss();
                    }
                }else if(s.equals("failed")){
                    Toast.makeText(Login.this, "Wrong pin",
                            Toast.LENGTH_LONG).show();
                    if (pd != null)
                    {
                        pd.dismiss();
                    }
                }else{
                    Toast.makeText(Login.this, "Username not found",
                            Toast.LENGTH_LONG).show();
                    if (pd != null)
                    {
                        pd.dismiss();
                    }
                }
            }
        }
        try {
            LoginUser luser = new LoginUser();
            luser.execute();
        }catch(Exception e){}
    }
}
