package com.example.bookwrittenby;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * FetchBook is an AsyncTask implementation that opens a network connection
 * and queryies the Book Service API.
 */
public class FetchBook extends AsyncTask<String, Void, String> {

    // Variables for the results TextViews.
    // These are WeakReferences to prevent "leaky context" -- weak references
    // enable the activity to be garbage collected if it is not needed.
    private WeakReference<TextView> titleWeakReferenceTextView;
    private WeakReference<TextView> authorWeakReferenceTextView;

    // Constructor, provides references to the views in MainActivity.
    public FetchBook(TextView titleText, TextView authorText) {
        this.titleWeakReferenceTextView = new WeakReference<>(titleText);
        this.authorWeakReferenceTextView = new WeakReference<>(authorText);
    }

    /**
     * Use the getBookInfo() method in the NetworkUtils class to make
     * the connection in the background.
     *
     * @param strings String array containing the search data.
     * @return Returns the JSON string from the Books API, or
     * null if the connection failed.
     */
    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON result and updates the views.
     *
     * @param s Result from the doInBackground() method containing the raw
     *          JSON response, or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Look for results in the items array, exiting when both the
            // title and author are found or when all items have been checked.
            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null) {
                titleWeakReferenceTextView.get().setText(title);
                authorWeakReferenceTextView.get().setText(authors);
            } else {
                // If none are found, update the UI to show failed results.
                titleWeakReferenceTextView.get().setText(R.string.no_results);
                authorWeakReferenceTextView.get().setText("");
            }

        } catch (Exception e) {
            // If onPostExecute() does not receive a proper JSON string,
            // update the UI to show failed results.
            titleWeakReferenceTextView.get().setText(R.string.no_results);
            authorWeakReferenceTextView.get().setText("");
            e.printStackTrace();
        }

    }
}
