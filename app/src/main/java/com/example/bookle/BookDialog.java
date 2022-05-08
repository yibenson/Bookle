package com.example.bookle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.bookle.databinding.BookDialogBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookDialog extends AppCompatActivity {

    BookDialogBinding bookDialogBinding;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private String amazonLink;
    private String title;
    private String author;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
    LocalDate localDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookDialogBinding = BookDialogBinding.inflate(getLayoutInflater());
        setContentView(bookDialogBinding.getRoot());

        int index = getIntent().getIntExtra("BOOK", 0);
        localDate = LocalDate.now().minusDays(index);

        getDatabaseValues();

        String text = "The Bookle on " + localDate.format(dateTimeFormatter) + " was...";
        bookDialogBinding.bookleMsg.setText(text);
        bookDialogBinding.bookleMsg.bringToFront();

        Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
        readerIntent.putExtra("DAY", localDate.toString());
        bookDialogBinding.book1Cover.setOnClickListener(view -> startActivity(readerIntent));

        bookDialogBinding.amazonButton.setOnClickListener(view -> open_link(index));
        bookDialogBinding.backButton.setOnClickListener(view -> finish());
        bookDialogBinding.shareButton.setOnClickListener(view -> clipboard(index));
        setDarkMode();
    }

    private void getDatabaseValues() {
        String day = localDate.toString();
        DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                .child("Books").child(day);

        databaseToday.child("title").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting title data", task.getException());
            }
            else {
                title = String.valueOf(task.getResult().getValue());
                bookDialogBinding.book1Title.setText(title);
            }
        });

        databaseToday.child("author").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting author data", task.getException());
            }
            else {
                author = String.valueOf(task.getResult().getValue());
                bookDialogBinding.book1Author.setText("by " + author);
            }
        });

        databaseToday.child("cover").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting cover data", task.getException());
            }
            else {
                String imageUri = String.valueOf(task.getResult().getValue());
                Picasso.get().load(imageUri).into(bookDialogBinding.book1Cover);
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

    private void open_link(int i) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonLink));
        startActivity(browserIntent);
    }

    private void clipboard(int i) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = getString(R.string.share_prior, localDate.format(dateTimeFormatter),
                title, author);

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

}

