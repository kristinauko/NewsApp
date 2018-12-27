package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public static String AUTHOR_SEPARATOR = "| ";

    /** Constructs new NewsAdapter */
    public NewsAdapter (Context context, List<News> newsList) {
        //Initialized ArrayAdapter's internal storage for context and the list
        super(context, 0, newsList);
    }

    /** Returns a list item view that displays info about news at the given position in the list */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main, parent, false);
        }

        News currentNewsItem = getItem(position);

        String titleAndAuthor = currentNewsItem.getTitle();

        String title;

        // Separate headline and author, if author's name is shown in the headline
        if (titleAndAuthor.contains(AUTHOR_SEPARATOR)) {
            String[] parts = titleAndAuthor.split("\\" + AUTHOR_SEPARATOR);
            title = parts[0];
        } else {
            title = titleAndAuthor;
        }

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(title);

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(currentNewsItem.getAuthor());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        sectionTextView.setText(currentNewsItem.getSection());

        Date pubDate = currentNewsItem.getPublicationDateTime();

        String formattedDate = "";
        if (pubDate != null) {
            formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pubDate);
        }

        // Create a new Date object
        // Find the TextView with view ID publishing_time
        TextView dateView = listItemView.findViewById(R.id.publishing_time);
        // Display the publishing_time of the current article in that TextView
        dateView.setText(formattedDate);

        return listItemView;
    }
}



