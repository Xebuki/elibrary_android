package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;

public class BookDetails extends AppCompatActivity implements Serializable {

    Library library = new Library();
    DBConnect dbConnect = new DBConnect();
    Connection conn;
    Context context = BookDetails.this;

    int position;
    int userID;
    Books book;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ImageView img = (ImageView) findViewById(R.id.book_details_imageView);
        TextView details_description = (TextView)findViewById(R.id.details_description);
        TextView details_author = (TextView)findViewById(R.id.details_author);
        TextView details_title = (TextView)findViewById(R.id.details_title);
        final Button details_add_button = (Button)findViewById(R.id.add_button);
        final Button details_comment_section = (Button)findViewById(R.id.comment_button);



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




//        getActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        if(b != null){
            book = (Books) getIntent().getSerializableExtra("book");

            System.out.println("POZYCJAAAAAAAAAAA: >>>> " + book.getFileName());
        }
        userID = getIntent().getIntExtra("userID", 0);


        details_title.setText("TITLE: " + book.getTitle());
        details_author.setText("AUTHOR: " + book.getAuthor());
        String desc = new String (dbConnect.description(conn, book.getTitle()));
        if(desc != null){
            details_description.setText(desc);
//            System.out.println(desc);
        }
        else{
            details_description.setText("No description available");
        }

        byte[] imgByte = book.getBookCover();;
        Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        img.setImageBitmap(bmp);

        details_add_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (userID > 0) {
                            int rs = dbConnect.addToLibrary(conn, book.getBookID(), userID);

                            switch (rs) {
                                case 0: {
                                    details_add_button.setText("Invalid login");break;
                                }
                                case 1: {
                                    details_add_button.setText("Book already in your library");break;
                                }
                                case 2: {
                                    details_add_button.setText("Successfully added to your library!");
                                    dbConnect.downloadBook(conn, BookDetails.this, book.getBookID());
                                    break;
                                }
                            }
                        }


                    }
                });
        details_comment_section.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent h = new Intent(BookDetails.this, CommentSection.class);
//                        h.putExtra("userID", userID);
                        h.putExtra("bookID", book.getBookID());
                        h.putExtra("userID", userID);
                        startActivity(h);
                    }
                });
        //end of on create
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    public byte[] loadBookCover(String file){
//        Blob bookCoverFile;
        ByteArrayOutputStream bos = null;

        try {
//            File cf = new File("/data/data/com.example.myapplication/files/" + file + "_cover.jpg");
            File cf = new File(this.getFilesDir().getPath() + "/" + file + "_cover.jpg");
            FileInputStream fis = new FileInputStream(cf);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
//                System.out.println(bos.size());
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());

        } catch (IOException e2) {
            System.err.println(e2.getMessage());

        }

        return bos != null ? bos.toByteArray() : null;
    }



}
