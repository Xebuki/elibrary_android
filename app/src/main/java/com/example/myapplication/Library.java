package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.folioreader.FolioReader;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;

public class Library extends AppCompatActivity implements Serializable, NavigationView.OnNavigationItemSelectedListener {
    String[] titles = null;
    ArrayList<Books> booksList = new ArrayList<>();
//    Books booksList = new Books();
    Connection conn;
    DBConnect dbConnect = new DBConnect();
    private DrawerLayout drawer;
//    Books books = new Books()
//    BookDetails bookDetails = new BookDetails();

    FolioReader folioReader = FolioReader.get();

    private int listPosition;

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private ViewStub stubGrid;
//    private ViewStub stubList;

//    private ListView listView;
    private GridView gridView;

//    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
//    private int currentViewMode = 0;

//    static final int VIEW_MODE_LISTVIEW = 0;
//    static final int VIEW_MODE_GRIDVIEW = 1;

    int userID = 0;
//FolioReader[] = ("file:///android_asset/TheSilverChair.epub");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute();

        userID = getIntent().getIntExtra("userID", 0);

        stubGrid = findViewById(R.id.stub_grid);
        stubGrid.inflate();
        gridView = findViewById(R.id.mygridview);

        //load all books from db
//        booksList = dbConnect.getBooks(Library.this, conn);
        //Get current view mode in share reference

        //Register item lick
        gridView.setOnItemClickListener(onItemClick);


//        switchView();
        stubGrid.setVisibility(View.VISIBLE);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of onCreate
    }

    private void setGridAdapter() {
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, booksList);
            gridView.setAdapter(gridViewAdapter);
    }



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
                Intent h = new Intent(Library.this, Library.class);
                h.putExtra("userID", userID);
                startActivity(h);
                break;
            case R.id.nav_logout:
                Intent g= new Intent(Library.this,Login.class);
                g.putExtra("isLogout", true);
                startActivity(g);
                break;
            case R.id.nav_userLibrary:
                Intent s= new Intent(Library.this,UsersLibrary.class);
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

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Do any thing when user click to item
            Intent h = new Intent(Library.this, BookDetails.class);

            h.putExtra("book", (Serializable) passBook(position));
            h.putExtra("userID", userID);
            startActivity(h);
        }
    };

    public Books passBook(int position){
        return booksList.get(position);
    }


    class DownloadTask extends AsyncTask<Void, Integer, ArrayList<Books>> {

//        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            progressBar = new ProgressBar(Library.this);
//            progressBar.


        }

        @Override
        protected ArrayList<Books> doInBackground(Void... voids) {
//            ArrayList<Books> books = dbConnect.getBooks(Library.this, conn);
            booksList = dbConnect.getBooks(Library.this, conn);
            System.out.println("SIZE: " + booksList.size());
            return booksList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Books> items) {
//            booksList = items;
//            gridViewAdapter.notifyDataSetChanged();
            setGridAdapter();

//            Toast.makeText(Library.this, booksList.size() , Toast.LENGTH_LONG).show();
        }
    }
}
