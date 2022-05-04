package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.bookle.databinding.ActivityBookTodayBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalTime;

public class BookToday extends AppCompatActivity {
    NumberFormat f = new DecimalFormat("00");
    ActivityBookTodayBinding binding;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookTodayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalTime localTime = LocalTime.now();
        LocalTime midnight = LocalTime.MIDNIGHT.minusNanos(1);
        long diff = Duration.between(localTime, midnight).toMillis();

        /* Retrieve today's cover image from database. */
        displayTodaysBook();

        setDarkMode();

        new CountDownTimer(diff, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (((millisUntilFinished / 1000) - seconds) / 60) % 60;
                long hours = ((((millisUntilFinished / 1000) - seconds) / 60 - minutes) / 60);
                String h = f.format(hours);
                String m = f.format(minutes);
                String s = f.format(seconds);
                String time = h + ":" + m + ":" + s;
                binding.timer.setText(time);
            }

            public void onFinish() {
                binding.timer.setText("done!");
            }

        }.start();
        binding.backButton.setOnClickListener(this::close);
        binding.amazonButton.setOnClickListener(view -> open_link());
        binding.shareButton.setOnClickListener(view -> clipboard());
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
        String today = "Books/" + "04-29-2022";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        ImageView cover = (ImageView) findViewById(R.id.book0_cover);
        cover.setBackground(AppCompatResources.getDrawable(this, R.drawable.prideprejudice));
        TextView title = (TextView) findViewById(R.id.book0_title);
        TextView author = (TextView) findViewById(R.id.book0_author);


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
        /*

        databaseReference.child(today + "/cover").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting cover data", task.getException());
            }
            else {
                String imageUri = String.valueOf(task.getResult().getValue());
                imageUri = "https://imgur.com/gallery/rNJGvT8";
                Picasso.get().load(imageUri).into(cover);
            }
        });

         */



    }

    public void close(View view) {
        finish();
    }

    private void clipboard() {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = getString(R.string.share_book);

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
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

    private void open_link() {
        String[] amazon_links = getResources().getStringArray(R.array.amazon_links);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazon_links[0]));
        startActivity(browserIntent);
    }

}