package com.example.myapplication;

import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

public class Basic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final AssetManager assetManager = getAssets();
        String[] source = null;
        try {
            // for assets folder add empty string
            source = assetManager.list("");
            // for assets/subFolderInAssets add only subfolder name
            String[] filelistInSubfolder = assetManager.list("subFolderInAssets");
            if (source == null) {
                // dir does not exist or is not a directory
            } else {
                for (int i=0; i<source.length; i++) {
                    // Get filename of file or directory
                    String filename = source[i];
                }
            }

            // if(filelistInSubfolder == null) ............

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        TextView tv=(TextView)findViewById(R.id.textView2);
        tv.setText(Arrays.toString(source).replaceAll("\\[|\\]", ""));
//        tv.setText("jeden dwa trzy:");

    }

}
