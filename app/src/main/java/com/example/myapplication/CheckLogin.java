package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckLogin extends AsyncTask<String, String, String> {
    String z = "";
    Boolean isSuccess = false;
    Connection conn;
    DBConnect dbConnect = new DBConnect();


    @Override
    protected String doInBackground(String... params){
//            String user_email = email.getText().toString();
//            String user_password = password.getText().toString();
        String user_email =  params[1];
        String user_password = params[0];
        conn = DBConnect.connectionclass();
        dbConnect.loginUser(conn, user_email, user_password);
//        if(dbConnect.loginUser(conn, user_email, user_password) > 0){
//            int userID = dbConnect.loginUser(conn, user_email, user_password);
//            Intent h;
//            h = new Intent(Login.class, CheckLogin.class);
//            h.putExtra("userID", userID);
//            startActivity(h);
//        }else{
//            Toast.makeText(Login.this,"Invalid login or password" , Toast.LENGTH_LONG).show();
//        }

        return z;
    }

}