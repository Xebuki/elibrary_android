package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class CommentSection extends AppCompatActivity {
    ListView listView ;
    DBConnect dbConnect = new DBConnect();
    Connection conn;

    Context context = CommentSection.this;
    EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        comment = (EditText) findViewById(R.id.comment_editText);
        comment.setHint("Enter comment");
        Button publish_comment_button = (Button)findViewById(R.id.publish_comment_button);
        final EditText commentTXT = (EditText)findViewById(R.id.comment_editText);

        listView = (ListView) findViewById(R.id.list);


        final int bookID = getIntent().getIntExtra("bookID", 0);
        final int userID = getIntent().getIntExtra("userID", 0);
        ArrayList<String> list;
        list = dbConnect.getAllComments(conn, bookID);
        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(this, R.layout.row, list);

        listView.setAdapter(adapter);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        publish_comment_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        commentTXT.getText().toString();
                        if(commentTXT == null){
                            Toast.makeText(CommentSection.this, "You can not post empty comment!", Toast.LENGTH_LONG).show();
                        }else{
                            if(dbConnect.addComment(conn, userID, bookID, commentTXT.getText().toString())){
                                Toast.makeText(CommentSection.this, "Comment is posted", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());

                            }else{
                                Toast.makeText(CommentSection.this, "Something went wrong", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }

}
