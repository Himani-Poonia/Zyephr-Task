package com.example.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    class bookValues {
        String name, author,category,publish_date,page_count;

        public bookValues(String name,String author, String category,String publish_date,String page_count) {
            this.name = name;
            this.author = author;
            this.category = category;
            this.publish_date = publish_date;
            this.page_count = page_count;
        }

        public String getName() {
            return name;
        }

        public String getAuthor() {
            return author;
        }

        public String getCategory() {
            return category;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public String getPage_count() {
            return page_count;
        }
    }

    class SortbyName implements Comparator<bookValues>
    {
        public int compare(bookValues a, bookValues b)
        {
            return a.name.compareTo(b.name);
        }
    }

    class SortbyAuthor implements Comparator<bookValues>
    {
        public int compare(bookValues a, bookValues b)
        {
            return a.author.compareTo(b.author);
        }
    }

    class SortbyCategory implements Comparator<bookValues>
    {
        public int compare(bookValues a, bookValues b)
        {
            return a.category.compareTo(b.category);
        }
    }

    class SortbyDate implements Comparator<bookValues>
    {
        public int compare(bookValues a, bookValues b)
        {
            String aYear = "", bYear = "";
            String aDate = a.getPublish_date();
            String bDate = b.getPublish_date();

            for(int i = aDate.length()-4; i < aDate.length(); i++) {
                aYear += aDate.charAt(i);
            }

            for(int i = bDate.length()-4; i < bDate.length(); i++) {
                bYear += bDate.charAt(i);
            }

            return aYear.compareTo(bYear);
        }
    }

    class SortbyPageCount implements Comparator<bookValues>
    {
        public int compare(bookValues a, bookValues b)
        {
            return a.page_count.compareTo(b.page_count);
        }
    }

    static ArrayList<bookValues> book = new ArrayList<>();
    static ArrayList<String> book_name = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SearchView searchView;
    ListView listView;

    String[] sortbyValues = {"Book Name","Author Name","Category","Publish Year","Page Count"};

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
                    .setItems(sortbyValues, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            switch (which) {

                                case 0:
                                    Collections.sort(book, new SortbyName());
                                    break;
                                case 1:
                                    Collections.sort(book, new SortbyAuthor());
                                    break;
                                case 2:
                                    Collections.sort(book, new SortbyCategory());
                                    break;
                                case 3:
                                    Collections.sort(book, new SortbyDate());
                                    break;
                                case 4:
                                    Collections.sort(book, new SortbyPageCount());
                                    break;
                                default:
                                    break;
                            }

                            getBookName();
                            arrayAdapter.notifyDataSetChanged();

                        }
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

    public void getBookName() {

        book_name.clear();

        for(int i = 0; i < book.size();i++) {
            book_name.add(book.get(i).getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("https://run.mocky.io/v3/a0528e65-80c9-4172-9231-876a622f25ef");

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,book_name);
        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position = book_name.indexOf(((ListView) parent).getAdapter().getItem(position).toString());

                Intent intent = new Intent(getApplicationContext(),Details.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

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

                if(book.size() == 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonPart = jsonArray.getJSONObject(i);

                        bookValues values = new bookValues(jsonPart.getString("book_name"), jsonPart.getString("author"),
                                jsonPart.getString("category"), jsonPart.getString("publish_date"), jsonPart.getString("page_count"));
                        book.add(values);

                        getBookName();
                        arrayAdapter.notifyDataSetChanged();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
//                Log.i("failed","++++++++++++++++++++++");
            }
        }
    }
}