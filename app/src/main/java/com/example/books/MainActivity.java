package com.example.books;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    protected ArrayList<SortClass.BookValues> book = new ArrayList<>();
    ArrayList<String> bookName = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SearchView searchView;
    ListView listView;

    protected void getBookName() {

        bookName.clear();

        for(int j = 0; j < book.size();j++) {
            bookName.add(book.get(j).getName());
        }
    }

    String[] sortbyValues = {"Book Name","Author Name","Category","Publish Year","Page Count"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiCall.DownloadTask task = new ApiCall.DownloadTask();
        task.execute("https://run.mocky.io/v3/a0528e65-80c9-4172-9231-876a622f25ef");

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,bookName);
        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position = bookName.indexOf(((ListView) parent).getAdapter().getItem(position).toString());

                Intent intent = new Intent(getApplicationContext(),Details.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();  //for sortby menu
        menuInflater.inflate(R.menu.main_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.sortby) {

            new AlertDialog.Builder(this)
                    .setTitle("Sort By")
                    .setItems(sortbyValues, (dialog, which) -> {


                        switch (which) {

                            case 0:
                                Collections.sort(book, new SortClass.SortbyName());
                                break;
                            case 1:
                                Collections.sort(book, new SortClass.SortbyAuthor());
                                break;
                            case 2:
                                Collections.sort(book, new SortClass.SortbyCategory());
                                break;
                            case 3:
                                Collections.sort(book, new SortClass.SortbyDate());
                                break;
                            case 4:
                                Collections.sort(book, new SortClass.SortbyPageCount());
                                break;
                            default:
                                break;
                        }

                        getBookName();
                        arrayAdapter.notifyDataSetChanged();

                    }).show();

            return true;
        }

        if(item.getItemId() == R.id.search) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    arrayAdapter.getFilter().filter(newText);
                    return true;
                }


            });

            return true;
        }

        return false;
    }

//    protected class DownloadTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//
//            String result = "";
//            URL url;
//            HttpsURLConnection connection = null;
//
//            try {
//
//                url = new URL(urls[0]);
//                connection = (HttpsURLConnection) url.openConnection();
//                InputStream in = connection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(in);
//                int data = reader.read();
//
//                while (data != -1) {
//
//                    result += (char) data;
//                    data = reader.read();
//
//                }
//
//                return result;
//
//            } catch (Exception e) {
//                e.printStackTrace();
////                Log.i("failed,","|||||||||||||||||||||||||||||||||||||");
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            try {
//
//                JSONObject jsonObject = new JSONObject(s);
//
//                String bookData = jsonObject.getString("data");
//
//                JSONArray jsonArray = new JSONArray(bookData);
//
//                if(book.size() == 0) {
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject jsonPart = jsonArray.getJSONObject(i);
//
//                        SortClass.BookValues values = new SortClass.BookValues(jsonPart.getString("book_name"), jsonPart.getString("author"),
//                                jsonPart.getString("category"), jsonPart.getString("publish_date"), jsonPart.getString("page_count"));
//
//                        book.add(values);
//
//                        getBookName();
//                        arrayAdapter.notifyDataSetChanged();
//
////                        Log.i("+++++++++++",mainActivity.book_name.get(i).toString());
//
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
////                Log.i("failed","++++++++++++++++++++++");
//            }
//        }
//    }

}