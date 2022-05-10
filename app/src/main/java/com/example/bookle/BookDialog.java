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

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
        readerIntent.putExtra("DAY", localDate.format(dateTimeFormatter));
        readerIntent.putExtra("SOURCE", "BOOKSHELF");
        bookDialogBinding.book1Cover.setOnClickListener(view -> startActivity(readerIntent));

        bookDialogBinding.amazonButton.setOnClickListener(view -> open_link());
        bookDialogBinding.backButton.setOnClickListener(view -> finish());
        bookDialogBinding.shareButton.setOnClickListener(view -> clipboard());

        Utils.setDarkMode(this);
    }

    private void getDatabaseValues() {
        String day = localDate.format(dateTimeFormatter);
        DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                .child("Books").child(day);

        Utils.databaseMethod actionTitle = new Utils.databaseMethod() {
            @Override
            public void method(String string) {
                title = string;
                bookDialogBinding.book1Title.setText(title);
            }
        };

        Utils.doFromDatabase(databaseToday, "title", actionTitle);

        Utils.databaseMethod actionAuthor = new Utils.databaseMethod() {
            @Override
            public void method(String string) {
                author = string;
                bookDialogBinding.book1Author.setText(getString(R.string.author, author));
            }
        };

        Utils.doFromDatabase(databaseToday, "author", actionAuthor);

        Utils.databaseMethod actionCover = (imageUri) ->
                Picasso.get().load(imageUri).into(bookDialogBinding.book1Cover);
        Utils.doFromDatabase(databaseToday, "cover", actionCover);

        Utils.databaseMethod actionBuy = (buy) ->
                amazonLink = buy;
        Utils.doFromDatabase(databaseToday, "buy", actionBuy);

    }

    private void open_link() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonLink));
        startActivity(browserIntent);
    }

    private void clipboard() {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        String text = getString(R.string.share_prior, localDate.format(dateTimeFormatter),
                title, author);

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

}

