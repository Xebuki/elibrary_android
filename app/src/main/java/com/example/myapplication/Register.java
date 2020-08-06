package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;

public class Register extends AppCompatActivity {

    DBConnect dbConnect = new DBConnect();
    Connection conn;
    EditText login, password, rpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_check_btn;

        login = (EditText) findViewById(R.id.register_login);
        login.setHint("Enter login");

        password = (EditText) findViewById(R.id.register_password);
        password.setHint("Enter password");

        rpassword = (EditText) findViewById(R.id.register_r_password);
        rpassword.setHint("Repeat password");

        register_check_btn = (Button) findViewById(R.id.register_check_button);

        register_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_login = login.getText().toString();
                String user_password = password.getText().toString();
                String user_r_password = rpassword.getText().toString();
            if(dbConnect.registerUser(conn, user_login, user_password, user_r_password)) {
                Intent h = new Intent(Register.this, Login.class);
                startActivity(h);
            }
            }
        });
    }




}
