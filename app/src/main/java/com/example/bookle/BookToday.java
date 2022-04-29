package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
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
        String today = "";
        today = setToday();
        if (today == "") {
            Log.e("date", "Error getting today's date");
        }
        // FIXME: Ereader date hardcoded here so it doesn't break!
        today = "04-27-2022";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(today + "/cover").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting cover data", task.getException());
            }
            else {
                String imageUri = String.valueOf(task.getResult().getValue());
                ImageView cover = (ImageView) findViewById(R.id.book0_cover);
                Picasso.get().load(imageUri).into(cover);
            }
        });

        new CountDownTimer(diff, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (((millisUntilFinished / 1000) - seconds)/ 60) % 60;
                long hours = ((((millisUntilFinished / 1000) - seconds)/ 60 - minutes)/ 60);
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

    private String setToday() {
        // Following code based on
        // https://medium.com/@shayma_92409/display-the-current-date-android-studio-f582bf14f908
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        return dateFormat.format(calendar.getTime());
    }
}