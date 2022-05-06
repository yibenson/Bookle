package com.example.bookle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.bookle.databinding.ActivityBookTodayBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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
import java.time.LocalDate;
import java.time.LocalTime;

public class BookToday extends AppCompatActivity {
    NumberFormat f = new DecimalFormat("00");
    ActivityBookTodayBinding binding;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    private String amazonLink;
    private String opener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookTodayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalTime localTime = LocalTime.now();
        LocalTime midnight = LocalTime.MIDNIGHT.minusNanos(1);
        long diff = Duration.between(localTime, midnight).toMillis();

        //Set cover, title, and author for today's Bookle
        getDatabaseValues();

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

    private void getDatabaseValues() {
        String today = LocalDate.now().toString();
        DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                .child("Books").child(today);

        databaseToday.child("title").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting title data", task.getException());
            }
            else {
                String raw = String.valueOf(task.getResult().getValue());
                binding.book0Title.setText(raw);
            }
        });

        databaseToday.child("author").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting author data", task.getException());
            }
            else {
                String raw = String.valueOf(task.getResult().getValue());
                binding.book0Author.setText("by " + raw);
            }
        });

        databaseToday.child("cover").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting cover data", task.getException());
            }
            else {
                String imageUri = String.valueOf(task.getResult().getValue());
                Picasso.get().load(imageUri).into(binding.book0Cover);
            }
        });

        databaseToday.child("buy").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting buy data", task.getException());
            }
            else {
                amazonLink = String.valueOf(task.getResult().getValue());
            }
        });

        databaseToday.child("opener").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting opener data", task.getException());
            }
            else {
                opener = String.valueOf(task.getResult().getValue());
            }
        });

    }

    public void close(View view) {
        Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
        String today = LocalDate.now().toString();
        readerIntent.putExtra("DAY", today);
        startActivity(readerIntent);
        finish();
    }

    private void clipboard() {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = getString(R.string.share_book, opener);

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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonLink));
        startActivity(browserIntent);
    }

}