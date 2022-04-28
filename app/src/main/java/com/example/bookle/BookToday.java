package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
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
}