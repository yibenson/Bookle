package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookToday extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_today);
        Calendar c = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date midnight = c.getTime();
        long diff = midnight.getTime() - currentTime.getTime();

        /* Retrieve today's cover image from database. */
        displayTodaysBook();

        setDarkMode();

        new CountDownTimer(diff, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (((millisUntilFinished / 1000) - seconds) / 60) % 60;
                long hours = ((((millisUntilFinished / 1000) - seconds) / 60 - minutes) / 60);
                TextView countdown = (TextView) findViewById(R.id.timer);
                String h = Long.toString(hours);
                String m = Long.toString(minutes);
                String s = Long.toString(seconds);
                String time = h + ":" + m + "." + s;
                countdown.setText(time);
            }

            public void onFinish() {
                TextView countdown = (TextView) findViewById(R.id.timer);
                countdown.setText("done!");
            }

        }.start();
    }

    private void displayTodaysBook() {
        /* Get today's date, which is saved in sharedPreferences. */
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        /*
        String today = sharedPref.getString("date", "");
        if (today == "") {
            Log.e("date", "Error getting today's date");
        }
        // FIXME: Ereader date hardcoded here so it doesn't break!

         */
        String today = "04-27-2022";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        ImageView cover = (ImageView) findViewById(R.id.book0_cover);
        TextView title = (TextView) findViewById(R.id.book0_title);
        TextView author = (TextView) findViewById(R.id.book0_author);

        cover.setBackground(getDrawable(R.drawable.cover1));
        title.setText("Where the Crawdads Sing");
        author.setText("by Delia Owens");

        /*

        databaseReference.child(today + "/title").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting title data", task.getException());
            }
            else {
                String raw = String.valueOf(task.getResult().getValue());
                title.setText(raw);
            }
        });

        databaseReference.child(today + "/author").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting author data", task.getException());
            }
            else {
                String raw = String.valueOf(task.getResult().getValue());
                author.setText("by " + raw);
            }
        });

        databaseReference.child(today + "/cover").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting cover data", task.getException());
            }
            else {
                String imageUri = String.valueOf(task.getResult().getValue());
                Picasso.get().load(imageUri).into(cover);
            }
        });

         */

    }

    public void close(View view) {
        finish();
    }

    public void setDarkMode() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean darkmode = sharedPref.getBoolean(getString(R.string.darkmode), false);
        if (darkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}