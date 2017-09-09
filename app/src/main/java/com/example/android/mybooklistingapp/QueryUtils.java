package com.example.android.mybooklistingapp;
import android.app.DownloadManager;
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
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    public QueryUtils() {
    }

    public static List<Book> fetchData(String queryutils){

        URL link = createLink(queryutils);

        String response = null;

        try {

            response = makeHttpRequest(link);
        } catch (IOException exception) {

            Log.e(LOG_TAG, "Error in the HTTP request", exception);
        }

        List<Book> books = extractFromJson(response);
        return books;
    }


    private static URL createLink(String queryutils) {
        URL link = null;
        try {
            link = new URL (queryutils);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "URL creation failed", exception);
        }
        return link;
    }

    private static String makeHttpRequest(URL link) throws IOException {
        String jsonResponse = "";
        if (link == null) {
            return jsonResponse;
        }
        HttpURLConnection connection = null;
        InputStream stream = null;

        try {
            connection = (HttpURLConnection) link.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                stream = connection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {

                Log.e(LOG_TAG, "Error Response Code: " + connection.getResponseCode());
            }
        } catch (IOException exception) {

            Log.e(LOG_TAG, "URL creation failed", exception);
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream stream) throws IOException {

        StringBuilder builder = new StringBuilder();

        if (stream != null) {
            InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Book> extractFromJson(String responseJson) {
        if (TextUtils.isEmpty(responseJson)) {
            return null;
        }

        List<Book> books = new ArrayList<Book>();
        try {
            JSONObject volumes = new JSONObject(responseJson);
            JSONArray items  = new JSONArray();
            if (volumes.has("items")) {
                items = volumes.getJSONArray("items");
            }
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String author = "Unknown author";
                if (volumes.has("authors")) {
                    author = volumes.getString("authors");
                }
                String year = "Unknown publication date";
                if (volumes.has("publishedDate")) {
                    year = volumes.getString("publishedDate");
                }
                String link = volumeInfo.getString("infoLink");
                Book book = new Book(title, author, year, link);
                books.add(book);
                Log.v("my_tag", "JSONResponse is: "+responseJson.toString());
            }
        } catch (JSONException exception) {
            Log.e(LOG_TAG, "An error occured while extracting data from the JSON response.", exception);
        }
        return books;
    }

}