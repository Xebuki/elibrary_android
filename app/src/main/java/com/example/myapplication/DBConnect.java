package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;

public class DBConnect {



    @SuppressLint("NewApi")
    public static java.sql.Connection connectionclass() {
        String user, password, database, server;

        server = "192.168.1.15";
        database = "Bookify";
        user = "sa";
        password = "123";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + "/" + database;
            connection = DriverManager.getConnection(ConnectionURL, user, password);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

    public ArrayList<Books> getBooks(Context context, Connection conn){

        ArrayList<Books> list = new ArrayList<>();
        try {

            conn = DBConnect.connectionclass();
            String query =
                    "SELECT id, bookID, bookFile, bookCover, author, title, fileName from Library";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while(rs.next()){

                String id = rs.getString("id");
                int bookID = rs.getInt("bookID");
                byte[] bookFile = null;
                Blob bookCover = rs.getBlob("bookCover");
                String author = rs.getString("author");
                String title = rs.getString("title");
                String fileName = rs.getString("fileName");

                byte[] byteBookCover;
                    try {
                        byteBookCover = bookCover.getBytes(1, (int) bookCover.length());

                    }catch(Exception e){
                        Log.e("Exception", "File write failed: " + e.toString());
                        byteBookCover = null;
                    }

                list.add(new Books(id, bookID, byteBookCover, bookFile, title, author, fileName));
//                Toast.makeText(context, "All books from db", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        return list;
    }

    public byte[] description(Connection conn, String title){

        byte[] description = null;
        try {

            conn = DBConnect.connectionclass();
            String query =
                    "SELECT description from Library WHERE title = '" + title + "'";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while(rs.next()){

                Blob blob = rs.getBlob("description");
                int blobLength = (int) blob.length();
                description = blob.getBytes(1, blobLength);
            }
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
            description = null;
        }



        return description;
    }

    public int addToLibrary(Connection conn, int bookID, int userID){
        int result = 0;
        try {

            conn = DBConnect.connectionclass();
            String query =
                    "SELECT * FROM Users_Books WHERE userID = " + userID + "AND bookID = " + bookID;
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            if(rs.next()){
                //book already added
                result = 1;
                System.out.println("Book already in library");

            }
            else{
                String insertQuery =
                        "INSERT INTO Users_Books values (" + userID + ", " + bookID + ")";
                state.executeUpdate(insertQuery);
                result = 2; //successfully added
                System.out.println("Successfully added to library");

            }
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());


        }

        return result;
    }

    public ArrayList<Books> getMyBooks(Connection conn, int userID){

        ArrayList<Books> list = new ArrayList<>();
        try {

            conn = DBConnect.connectionclass();
            String query =
                    "SELECT Library.id, Library.bookID, Library.author, Library.title, Library.fileName FROM Library\n" +
                            "INNER JOIN Users_Books ON Library.bookID = Users_Books.bookID\n" +
                            "WHERE Users_Books.userID = " + userID;
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while(rs.next()){

                String id = rs.getString("id");
                int bookID = rs.getInt("bookID");
                String author = rs.getString("author");
                String title = rs.getString("title");
                String fileName = rs.getString("fileName");

                list.add(new Books(id, bookID,null, null, title, author, fileName));
            }
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        return list;
    }

    public ArrayList<String> getAllComments(Connection conn, int bookID){

        ArrayList<String> list = new ArrayList<>();
        try {
            conn = DBConnect.connectionclass();
            String query = "SELECT comment FROM Users_comments WHERE bookID= " + bookID;
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while(rs.next()){
                String comment = rs.getString("comment");
                System.out.println("Comment added");
                list.add(comment);
            }
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        return list;
    }

    public void downloadBook(Connection conn, Context context, int bookID) {
        byte[] bookFileBytes, bookCoverBytes;
        String query;

        try {

            conn = DBConnect.connectionclass();
            query =
                    "SELECT bookFile, bookCover, fileName from Library where bookID =" + bookID;
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);
            if (rs.next()) {
                Blob bookFile = rs.getBlob("bookFile");
                Blob bookCover = rs.getBlob("bookCover");
                String fileName = rs.getString("fileName");
                bookFileBytes = bookFile.getBytes(1, (int) bookFile.length());
                bookCoverBytes = bookCover.getBytes(1, (int) bookCover.length());
                FileOutputStream fos = context.openFileOutput(fileName + ".epub", MODE_APPEND);
                fos.write(bookFileBytes);

                fos = context.openFileOutput(fileName + "_cover.jpg", MODE_APPEND);
                fos.write(bookCoverBytes);
                fos.close();
                System.out.println("Saved");
//                Toast.makeText(context, "saved" + context.getFilesDir(), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }




        //end of getBook
    }

    public Integer loginUser(Connection conn, String user_username, String user_password) {
        String z = "";
        Boolean isSuccess = false;
        int userID = 0;

        if(user_username.trim().equals("") || user_password.trim().equals("")){
            z = "Please enter email and password";
        }
        else{
            try{
                conn = DBConnect.connectionclass();
                if (conn == null){
                    z = "Check Your internet access!";
                }
                else{
                    String query = "select UserID from Users where user_email = '" + user_username + "' and user_password = '" + user_password + "'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next()){
                        z = "Login successful";
                        userID = rs.getInt("UserID");
                        conn.close();
                    }
                    else{
                        z = "Invalid credentials";

                    }
                }
            }
            catch (Exception ex){
                isSuccess = false;
                z = ex.getMessage();
            }
        }
        System.out.println(z);
        return userID;
    }

    public Boolean deleteBook(Connection conn, Context context, int bookID, int userID, String filename){

        try {

            conn = DBConnect.connectionclass();
            String query =
                    "DELETE FROM Users_Books WHERE userID = " + userID + "AND bookID = " + bookID;
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String name = "/" + filename;
        File cover = new File(context.getFilesDir() + File.separator + filename + "_cover.jpg");
        File bookFile = new File(context.getFilesDir() + File.separator + filename + ".epub");
        cover.delete();
        bookFile.delete();

        return true;
    }

    public Boolean registerUser(Connection conn, String user_email, String user_password, String user_r_password){

        String z = "";
        Boolean isSuccess = false;
        conn = DBConnect.connectionclass();
        if(user_email.trim().equals("") || user_password.trim().equals("") || user_r_password.trim().equals("")){
            z = "Please fill all values";
        }else if(!user_password.equals(user_r_password)){
            z = "Passwords doesn't match";
        }else{
            try{
                conn = DBConnect.connectionclass();
                if (conn == null){
                    z = "Check Your internet access!";
                }
                else{
                    String query = "select user_email from Users where user_email = '" + user_email + "'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next()){
                        z = "Login already in database";
//                        conn.close();
                    }
                    else{
                    String register_query = "INSERT INTO Users(user_email, user_password) VALUES ('" + user_email + "', '" + user_password + "')";
                    stmt.executeUpdate(register_query);
                    isSuccess = true;
                    conn.close();
                    }
                }
            }
            catch (Exception ex){
                isSuccess = false;
                z = ex.getMessage();
            }
        }


        System.out.println(z);
        return isSuccess;
    }

    public Boolean addComment(Connection conn, int userID, int bookID, String comment){
        Boolean isSuccess = false;
        if(userID == 0 || bookID == 0 ){
            return false;
        }
        try{
            conn = DBConnect.connectionclass();
            if (conn == null){
                return false;
            }
            else{
                Statement stmt = conn.createStatement();
                String comment_query = "INSERT INTO Users_comments(userID, bookID, comment) VALUES (" + userID + ", " + bookID + ", " + "'" + comment + "')" ;
                stmt.executeUpdate(comment_query);
                conn.close();
            }
        }

        catch (Exception ex){
            return false;
        }
        return true;
    }
}