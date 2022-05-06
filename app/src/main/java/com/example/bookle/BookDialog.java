package com.example.bookle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.bookle.databinding.BookDialogBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookDialog extends AppCompatActivity {

    BookDialogBinding bookDialogBinding;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    // TODO: Hardcoded Bookle
    int[] post_reveal_covers = new int[]{ R.drawable.prideprejudice, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
    LocalDate localDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookDialogBinding = BookDialogBinding.inflate(getLayoutInflater());
        setContentView(bookDialogBinding.getRoot());

        int index = getIntent().getIntExtra("BOOK", 0);
        localDate = LocalDate.now().minusDays(index);

        // TODO: Hardcoded Bookle
        bookDialogBinding.book1Cover.setBackground(AppCompatResources.getDrawable(this, post_reveal_covers[index]));
        bookDialogBinding.book1Author.setText("By " + getResources().getStringArray(R.array.authors)[index]);
        bookDialogBinding.book1Title.setText(getResources().getStringArray(R.array.titles)[index]);
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
        // TODO: Hardcoded Bookle
        String[] amazon_links = getResources().getStringArray(R.array.amazon_links);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazon_links[i]));
        startActivity(browserIntent);
    }

    private void clipboard(int i) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        // TODO: Hardcoded Bookle
        String text = getString(R.string.share_prior, localDate.format(dateTimeFormatter),
                getResources().getStringArray(R.array.titles)[i],
                getResources().getStringArray(R.array.authors)[i]);

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

}

