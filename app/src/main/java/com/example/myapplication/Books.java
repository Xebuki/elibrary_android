package com.example.myapplication;

import android.util.Log;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

class Books implements Serializable {


    private String id;
    private int bookID;
    private byte[] bookCover;
    private byte[] bookFile;
    private String title;
    private String author;
    private String fileName;
//    private String description;

    public Books(String id, int bookID, byte[] bookCover, byte[] bookFile, String title, String author, String fileName) {
        this.id = id;
        this.bookID = bookID;
        this.bookCover = bookCover;
        this.bookFile = bookFile;
        this.title = title;
        this.author = author;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public byte[] getBookCover() {
        return bookCover;
    }

    public void setBookCover(byte[] bookCover) {
        this.bookCover = bookCover;
    }

    public byte[] getBookFile() {
        return bookFile;
    }

    public void setBookFile(byte[] bookFile) {
        this.bookFile = bookFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //    public byte[] getByteCover(){
//        byte[] byteBookCover;
//        try {
//            byteBookCover = bookCover.getBytes(1, (int) bookCover.length());
//
//        }catch(Exception e){
//            Log.e("Exception", "File write failed: " + e.toString());
//            byteBookCover = null;
//        }
//        System.out.println("Cover loaded");
//        return byteBookCover;
//    }
}


