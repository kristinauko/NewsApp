package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public final class QueryUtils {

    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /**
     * Query the dataset and return a list of news objects
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create Url object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        //Extract relevant fields from JSON response and create a list of News
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Return the list of news
        return newsList;
    }

    /**
     * Returns new URL object from the given String
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Make HTTP request to given URL and return a String as the response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early;
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<News> extractFeatureFromJson(String newsJSON) {
        // If JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList to start adding news to
        List<News> newsItems = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of News objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject results = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = results.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNewsItem = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = currentNewsItem.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String section = currentNewsItem.getString("sectionName");

                // Extract the value for the key called "webUrl"
                String url = currentNewsItem.getString("webUrl");

                // Extract the value for the key called "webPublicationDate"
                String stringDateTime = currentNewsItem.getString("webPublicationDate");
                // Create Date pubDateTime
                Date pubDateTime = fromISO8601UTC(stringDateTime, "yyyy-MM-dd'T'HH:mm:ss'Z'") ;

                //Extract the JSONArray with the key "tag"
                JSONArray tagsArray = currentNewsItem.getJSONArray("tags");

                //Declare String variable to hold author name
                String author = "";

                if (tagsArray.length() == 1) {
                    JSONObject contributorTag = (JSONObject) tagsArray.get(0);
                    author = contributorTag.getString("webTitle");
                }

                News news = new News(title, author, section, url, pubDateTime);
                newsItems.add(news);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of newsItems
        return newsItems;
    }

    /**
     * Format date
     */
    public static Date fromISO8601UTC(String dateStr, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}




