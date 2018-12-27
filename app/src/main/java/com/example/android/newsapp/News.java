package com.example.android.newsapp;

public class News {

    /** Title of news item*/
    private String mTitle;

    /** Author of news item*/
    private String mAuthor;

    /** Section of news item*/
    private String mSection;

    /** Link to news item*/
    private String mURL;

    /** Time of news item*/
    private java.util.Date mPublicationDateTime;

    /** Create a new News Object
     *
     * @param title is the title of news item
     * @param section is the section of news item
     * @param url is the lint to news item
     * @param publicationDateTime is the publication time and date
     * @param author is the author of news item
     *
     * */
    public News(String title, String author, String section, String url, java.util.Date publicationDateTime) {
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mURL = url;
        mPublicationDateTime = publicationDateTime;
    }

    /**
     * Get title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Get author
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Get section
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get author
     */
    public String getURL() {
        return mURL;
    }

    /**
     * Get time and date
     */
    public java.util.Date getPublicationDateTime() {
        return mPublicationDateTime;
    }
}
