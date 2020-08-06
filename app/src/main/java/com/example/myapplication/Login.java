package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

public class Login extends AppCompatActivity {

    EditText username, password;
    Connection conn;
    DBConnect dbConnect = new DBConnect();
    boolean isLogout;

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext();
        setContentView(R.layout.activity_login);


        isLogout = getIntent().getBooleanExtra("isLogout", false);
        Button login_btn, register_btn;
        login_btn = (Button) findViewById(R.id.login_button);
        register_btn = (Button) findViewById(R.id.register_button);

        username = (EditText) findViewById(R.id.login_username);
        username.setHint("Enter login");

        password = (EditText) findViewById(R.id.login_password);
        password.setHint("Enter password");

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        if(isLogout == true){
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            username.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_email = username.getText().toString();
                String user_password = password.getText().toString();
                if (saveLoginCheckBox.isChecked()) {
                    int userID = dbConnect.loginUser(conn, user_email, user_password);
                    if(userID != 0){
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", user_email);
                    loginPrefsEditor.putString("password", user_password);
                    loginPrefsEditor.putInt("userID", userID);
                    loginPrefsEditor.commit();

                    Intent h = new Intent(Login.this, UsersLibrary.class);
                    h.putExtra("userID", userID);
                    startActivity(h);
                    }
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();

                    conn = DBConnect.connectionclass();
                    if(dbConnect.loginUser(conn, user_email, user_password) > 0){
                        int userID = dbConnect.loginUser(conn, user_email, user_password);
                        Intent h = new Intent(Login.this, UsersLibrary.class);
                        h.putExtra("userID", userID);
                        startActivity(h);
                    }else{
                        Toast.makeText(Login.this,"Invalid login or password" , Toast.LENGTH_LONG).show();
                    }
                }


//                checkLogin.execute("");
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent h = new Intent(Login.this, Register.class);
                    startActivity(h);
            }
        });


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //end of onCreate
    }



}


