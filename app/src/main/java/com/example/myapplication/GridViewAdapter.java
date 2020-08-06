package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.folioreader.FolioReader;

import java.io.IOException;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Books> {

    public GridViewAdapter(Context context, int resource, List<Books> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }
        Books books = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
//        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
//        TextView txtDescription = (TextView) v.findViewById(R.id.txtGenre);


//        txtTitle.setText(books.getTitle());
        if(books.getBookCover() != null) {
            byte[] imgByte = books.getBookCover();
            Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            img.setImageBitmap(bmp);
        }
        return v;
    }
    }






/* FolioReader folioReader = FolioReader.get(); */
//    List<FolioReader> Source;
//    List<String> booksList;
//    Context mContext;






//    @Override
//    public int getCount() {
//        return booksList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return booksList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Button button;
//        if(convertView == null){
//            button = new Button(mContext);
//            button.setLayoutParams(new GridView.LayoutParams(85,85));
//            button.setPadding(8,8,8,8);
//            button.setText((CharSequence) booksList.get(position));
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//        else
//            button = (Button)convertView;
//        return button;
//    }
//}
