package com.example.books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int index = intent.getIntExtra("placeNumber",0);

        TextView detailsTextView = findViewById(R.id.valuesTextView);
        String details = "Book: ";

        MainActivity mainActivity = new MainActivity();

        details += mainActivity.book.get(index).getName() + "\nAuthor: ";
        details += mainActivity.book.get(index).getAuthor() + "\nCategory: ";
        details += mainActivity.book.get(index).getCategory() + "\nPublish Date: ";
        details += mainActivity.book.get(index).getPublishDate() + "\nPage Count: ";
        details += mainActivity.book.get(index).getPageCount() + "\n";

        detailsTextView.setText(details);
    }
}