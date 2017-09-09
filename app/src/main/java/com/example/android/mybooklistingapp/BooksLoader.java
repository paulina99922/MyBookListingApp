package com.example.android.mybooklistingapp;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Book>> {

    private String mQueryUtils;

    public BooksLoader(Context context, String queryutils) {
        super(context);
        mQueryUtils = queryutils;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mQueryUtils == null) {
            return null;
        }
        List<Book> books = QueryUtils.fetchData(mQueryUtils);

        return books;
    }
}
