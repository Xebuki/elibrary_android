package com.example.myapplication;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by NgocTri on 10/22/2016.
 */

public class ListViewAdapter extends ArrayAdapter<Books> {
    public ListViewAdapter(Context context, int resource, List<Books> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        Books books = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtGenre);

//        img.setImageResource(product.getImageId());
        txtTitle.setText(books.getTitle());
//        img.setImageBitmap(books.getBookCover());
//        txtDescription.setText(product.getDescription());

        byte[] imgByte = books.getBookCover();
        Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        img.setImageBitmap(bmp);

        return v;
    }
}
