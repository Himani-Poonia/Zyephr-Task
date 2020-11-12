package com.example.books;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiCall {

    protected static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpsURLConnection connection = null;

            try {

                url = new URL(urls[0]);
                connection = (HttpsURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {

                    result += (char) data;
                    data = reader.read();

                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
//                Log.i("failed,","|||||||||||||||||||||||||||||||||||||");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);

                String bookData = jsonObject.getString("data");

                JSONArray jsonArray = new JSONArray(bookData);
                MainActivity mainActivity = new MainActivity();

                if(mainActivity.book.size() == 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonPart = jsonArray.getJSONObject(i);

                        SortClass.BookValues values = new SortClass.BookValues(jsonPart.getString("book_name"), jsonPart.getString("author"),
                                jsonPart.getString("category"), jsonPart.getString("publish_date"), jsonPart.getString("page_count"));

                        mainActivity.book.add(values);

                        mainActivity.getBookName();
                        mainActivity.arrayAdapter.notifyDataSetChanged();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
//                Log.i("failed","++++++++++++++++++++++");
            }
        }
    }

}
