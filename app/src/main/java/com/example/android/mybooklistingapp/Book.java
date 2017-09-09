package com.example.android.mybooklistingapp;

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mYear;
    private String mLink;


    public Book(String title, String author, String year, String link) {
        mTitle = title;
        mAuthor = author;
        mYear = year;
        mLink = link;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {return mAuthor;}

    public String getYear() {return mYear;}

    public String getLink() {return mLink;}
}
