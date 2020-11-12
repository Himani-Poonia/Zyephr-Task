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

        details += MainActivity.book.get(index).getName() + "\nAuthor: ";
        details += MainActivity.book.get(index).getAuthor() + "\nCategory: ";
        details += MainActivity.book.get(index).getCategory() + "\nPublish Date: ";
        details += MainActivity.book.get(index).getPublish_date() + "\nPage Count: ";
        details += MainActivity.book.get(index).getPage_count() + "\n";

        detailsTextView.setText(details);
    }
}