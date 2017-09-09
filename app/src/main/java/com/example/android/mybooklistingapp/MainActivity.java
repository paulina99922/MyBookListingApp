package com.example.android.mybooklistingapp;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String mQueryUtils;
    private BookAdapter mAdapter;
    private SearchView searchView;
    private TextView emptyView1;
    private TextView emptyView2;
    private static final String BOOKS_LINK = "https://www.googleapis.com/books/v1/volumes?q=";
    public static String LOG_STATEMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.search_view);
        ListView bookListView = (ListView) findViewById(R.id.list_view);
        emptyView1 = (TextView) findViewById(R.id.empty_view_1);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book nBook = mAdapter.getItem(position);
                Uri link = Uri.parse(nBook.getLink());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, link);
                startActivity(webIntent);
            }
        });

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);

        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            emptyView1.setText(R.string.no_connection);
            emptyView2.setVisibility(View.GONE);
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isConnected()) {
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                    return true;
                } else {
                    mAdapter.clear();
                    emptyView1.setText(R.string.no_connection);
                    emptyView2.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String getList = "";
        if (mQueryUtils != null && mQueryUtils != "") {
            getList = BOOKS_LINK + mQueryUtils;
        } else {
            String defaultQuery = "android";
            getList = BOOKS_LINK + defaultQuery;
        }
        Log.v(LOG_STATEMENT, "Book url being queried is :" + getList );
        return new BooksLoader(this, BOOKS_LINK + mQueryUtils);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
        emptyView1.setText(R.string.no_book_found);
        emptyView2.setText(R.string.use_other_keywords);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mQueryUtils = searchView.getQuery().toString();
        outState.putString("queryutils", mQueryUtils);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mQueryUtils = savedInstanceState.getString("queryutils");
        super.onRestoreInstanceState(savedInstanceState);
    }
}


