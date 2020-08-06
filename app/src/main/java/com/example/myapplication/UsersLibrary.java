package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.folioreader.Config;
import com.folioreader.FolioReader;
import com.folioreader.model.locators.ReadLocator;
import com.folioreader.util.AppUtil;
import com.folioreader.util.ReadLocatorListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.folioreader.FolioReader;
import com.folioreader.model.HighLight;
import com.folioreader.model.locators.ReadLocator;
import com.folioreader.ui.base.OnSaveHighlight;
import com.folioreader.util.AppUtil;
import com.folioreader.util.OnHighlightListener;
import com.folioreader.util.ReadLocatorListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
//import javax.sql.rowset.serial.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersLibrary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReadLocatorListener, FolioReader.OnClosedListener{

    private static final String LOG_TAG = UsersLibrary.class.getSimpleName();

    private DrawerLayout drawer;


    DBConnect dbConnect = new DBConnect();
    Connection conn;
    int userID;
    int bookPosition;
    private ViewStub stubGrid;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private String thisFileName;

    private FolioReader folioReader;
    ArrayList<Books> myBooksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_library);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        folioReader = FolioReader.get()
                .setOnClosedListener(this)
                .setReadLocatorListener(this);

        userID = getIntent().getIntExtra("userID", 0);
        if(userID > 0) {
            myBooksList = dbConnect.getMyBooks(conn, userID);
            for (int i = 0; i < myBooksList.size(); i++) {
                myBooksList.get(i).setBookCover(loadBookCover(myBooksList.get(i).getFileName()));
            }
            stubGrid = findViewById(R.id.stub_grid);

            stubGrid.inflate();
            gridView = findViewById(R.id.mygridview);
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, myBooksList);
            gridView.setAdapter(gridViewAdapter);
            gridView.setOnItemClickListener(onItemClick);
            gridView.setOnItemLongClickListener(onItemLongClickListener);
        }
        else{
                    Toast.makeText(this, "There are no books in your library", Toast.LENGTH_LONG).show();
        }


        System.out.println("User ID = " + userID);
//        Toast.makeText(this, myBooksList.get(1).getAuthor(), Toast.LENGTH_LONG).show();
//        Toast.makeText(this, userID, Toast.LENGTH_LONG).show();


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    //end of on create
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Do any thing when user click to item
            Toast.makeText(getApplicationContext(), myBooksList.get(position).getTitle() + " - " + myBooksList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//            folioReader.openBook("/data/data/com.example.myapplication/files/" + myBooksList.get(position).getFileName() + ".epub");
            ReadLocator readLocator = getLastReadLocator(myBooksList.get(position).getFileName());

            setThisFileName(myBooksList.get(position).getFileName());

            Config config = AppUtil.getSavedConfig(getApplicationContext());
            if (config == null)
                config = new Config();
            config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);

            folioReader.setReadLocator(readLocator);
            folioReader.setConfig(config, true)
                    .openBook("/data/data/com.example.myapplication/files/" + myBooksList.get(position).getFileName() + ".epub");

        }
    };

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(UsersLibrary.this, "tak", Toast.LENGTH_LONG).show();
            bookPosition = position;
            System.out.println("pozycja: " + position);
            AlertDialog.Builder builder = new AlertDialog.Builder(UsersLibrary.this, R.style.DialogCustomTheme);
            builder.setMessage("Are you sure you want to delete this book?");
//            builder.setMessage("Are you sure you want to delete this book?").setPositiveButton("Yes", dialogClickListener)
//                    .setNegativeButton("No", dialogClickListener).show();


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing, but close the dialog
                    System.out.println("Clicked yes" + myBooksList.get(bookPosition).getBookID());
                    dbConnect.deleteBook(conn, getApplicationContext(), myBooksList.get(bookPosition).getBookID(), userID, myBooksList.get(bookPosition).getFileName());
                    finish();
                    startActivity(getIntent());
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    //System.out.println("Clicked no" + myBooksList.get(bookPosition).getBookID());
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }


    };





//    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which){
//                case DialogInterface.BUTTON_POSITIVE:
////                    dbConnect.deleteBook(conn, getApplicationContext(), userID, myBooksList.get(bookPosition).getBookID());
//                    System.out.println("Clicked yes" + myBooksList.get(bookPosition).getBookID());
//                    break;
//
//                case DialogInterface.BUTTON_NEGATIVE:
//                    //No button clicked
//                    System.out.println("Clicked no" + myBooksList.get(bookPosition).getBookID());
//                    break;
//            }
//        }
//    };




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_library:
                Intent h = new Intent(UsersLibrary.this, Library.class);
                h.putExtra("userID", userID);
                startActivity(h);
                break;
            case R.id.nav_logout:
                Intent g= new Intent(UsersLibrary.this,Login.class);
                g.putExtra("isLogout", true);
                startActivity(g);
                break;
            case R.id.nav_userLibrary:
                Intent s= new Intent(UsersLibrary.this,UsersLibrary.class);
                s.putExtra("userID", userID);
                startActivity(s);
                break;
//            case R.id.nav_slideshow:
//                Intent s= new Intent(Home.this,Slideshow.class);
//                startActivity(s);
//            case R.id.nav_tools:
//                Intent t= new Intent(Home.this,Tools.class);
//                startActivity(t);
//                break;
            // this is done, now let us go and intialise the home page.
            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    private String readLocator(String Filename) {

        String ret = "";
        String bookName = getThisFileName();
        InputStream inputStream = null;
        try {
            inputStream = openFileInput("last_read_locator_" + bookName + ".json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("READ LOCATOR", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("READ LOCATOR", "Can not read file: " + e.toString());
        }
        finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        return ret;
    }



    private ReadLocator getLastReadLocator(String Filename) {

//        String jsonString = loadAssetTextAsString("Locators/LastReadLocators/last_read_locator_1.json");
        String jsonString = readLocator(Filename);
//        if (jsonString.isEmpty()){
//            System.out.println("NO READ LOCATOR!");
//        }
//        System.out.println("LAST READ LOCATOR!");
        return ReadLocator.fromJson(jsonString);
    }

    @Override
    public void saveReadLocator(ReadLocator readLocator) {
        Log.i(LOG_TAG, "-> saveReadLocator -> " + readLocator.toJson());
        System.out.println("READ LOCATOR SAVED!: " + readLocator.toJson());

        String bookName = getThisFileName();
        try {
            String filename = "last_read_locator_" + bookName + ".json";
            FileOutputStream fos = this.openFileOutput(filename, MODE_PRIVATE);
            fos.write(readLocator.toJson().getBytes());
            fos.close();
            System.out.println("Saved");
        } catch (FileNotFoundException fileNotFound) {

        } catch (IOException ioException) {

        }
    }


    private String loadAssetTextAsString(String name) {
        BufferedReader inn = null;
        try {
            StringBuilder buf = new StringBuilder();
//            InputStream is = getAssets().open(name);
            InputStream is = getAssets().open(name);
            inn = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str = inn.readLine()) != null) {
                System.out.println("IS FIRST = " + str);
                if (isFirst) {
                    isFirst = false;

                }
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e("UsersLibrary", "Error opening asset " + name + " ");
            e.printStackTrace();
        } finally {
            if (inn != null) {
                try {
                    inn.close();
                } catch (IOException e) {
                    Log.e("UsersLibrary", "Error closing asset " + name);
                }
            }
        }
        return null;
    }

    public String getThisFileName(){
        return thisFileName;
    }

    public void setThisFileName(String thisFileName) {
        this.thisFileName = thisFileName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FolioReader.clear();
    }

    @Override
    public void onFolioReaderClosed() {
        Log.v(LOG_TAG, "-> onFolioReaderClosed");
    }

}
