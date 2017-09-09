package com.example.android.mybooklistingapp;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_position, parent, false);
        }

        Book nBook = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.title);
        titleTextView.setText(nBook.getTitle());

        TextView authorTextView = listItemView.findViewById(R.id.author);
        authorTextView.setText(nBook.getAuthor());

        TextView yearTextView = listItemView.findViewById(R.id.year);
        yearTextView.setText(nBook.getYear());

        TextView linkTextView = listItemView.findViewById(R.id.link);
        linkTextView.setText(nBook.getLink());

        return listItemView;

    }
}

