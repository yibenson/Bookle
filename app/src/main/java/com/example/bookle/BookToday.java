package com.example.bookle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookle.databinding.ActivityBookTodayBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BookToday extends AppCompatActivity {
    NumberFormat f = new DecimalFormat("00");
    ActivityBookTodayBinding binding;

    private String amazonLink;
    private String opener;

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

        Utils.setDarkMode(this);

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
        binding.appName.setOnClickListener(this::close);
    }

    private void getDatabaseValues() {
        String today = LocalDate.now().toString();
        DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                .child("Books").child(today);

        Utils.databaseMethod actionTitle = (title) -> binding.bookTodayTitle.setText(title);
        Utils.doFromDatabase(databaseToday, "title", actionTitle);

        Utils.databaseMethod actionAuthor = (author) ->
                binding.bookTodayAuthor.setText(getString(R.string.written_by, author));
        Utils.doFromDatabase(databaseToday, "author", actionAuthor);

        Utils.databaseMethod actionCover = (imageUri) ->
                Picasso.get().load(imageUri).into(binding.book0Cover);
        Utils.doFromDatabase(databaseToday, "cover", actionCover);

        Utils.databaseMethod actionBuy = (buy) -> amazonLink = buy;
        Utils.doFromDatabase(databaseToday, "buy", actionBuy);

        Utils.databaseMethod actionOpener = (o) -> opener = o;
        Utils.doFromDatabase(databaseToday, "opener", actionOpener);

    }

    public void close(View view) {
        Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
        String today = LocalDate.now().format(Utils.dateFormatInternal);
        readerIntent.putExtra(getString(R.string.DAY), today);
        startActivity(readerIntent);
        finish();
    }

    private void clipboard() {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = getString(R.string.today_book_share, opener);

        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

    private void open_link() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonLink));
        startActivity(browserIntent);
    }

}