package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Book5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book5);
    }
    public void close(View view) {
        if (Reader.from_reader == 1) {
            Reader.from_reader = 0;
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            if (Bookshelf.from_bookshelf == 1) {
                Bookshelf.from_bookshelf = 0;
                startActivity(new Intent(getApplicationContext(), Bookshelf.class));
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }
}