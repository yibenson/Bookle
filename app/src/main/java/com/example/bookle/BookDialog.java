package com.example.bookle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.bookle.databinding.BookDialogBinding;
import com.example.bookle.databinding.EreaderBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class BookDialog extends AppCompatActivity {

    BookDialogBinding bookDialogBinding;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    int[] post_reveal_covers = new int[]{ R.drawable.cover1, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};

    String[] authors = new String[]{"Delia Owens", "Michelle Zauner", "Michelle Obama", "Matt Haig", "Martha Stout", "Naomi Novik", "Jennifer Egan",
    "Taylor Jenkins Reid", "Lucy Foley", "Suleika Jaouad", "Colleen Hoover", "Emily St. John Mandel", "Brit Bennett", "Heidi W. Durrow"};

    String[] titles = new String[]{"Where the Crawdads Sing", "Crying in H Mart", "Becoming", "The Midnight Library", "The Sociopath Next Door", "The Graduate", "The Candy House",
    "The Seven Husbands of Evelyn Hugo", "The Paris Apartment", "Between Two Kingdoms", "Reminders of Him", "Sea of Tranquility", "The Vanishing Heir", "The Girl Who Fell From The Sky"};

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
    LocalDate localDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookDialogBinding = BookDialogBinding.inflate(getLayoutInflater());
        setContentView(bookDialogBinding.getRoot());
        int index = getIntent().getIntExtra("BOOK", 0);
        localDate = LocalDate.now().minusDays(index);
        bookDialogBinding.book1Cover.setBackground(AppCompatResources.getDrawable(this, post_reveal_covers[index]));
        bookDialogBinding.book1Author.setText("By " + authors[index]);
        bookDialogBinding.book1Title.setText(titles[index]);
        String text = "The Bookle on " + localDate.format(dateTimeFormatter) + " was...";
        bookDialogBinding.bookleMsg.setText(text);
        bookDialogBinding.bookleMsg.bringToFront();
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
        String[] amazon_links = getResources().getStringArray(R.array.amazon_links);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazon_links[i]));
        startActivity(browserIntent);
    }

    private void clipboard(int i) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = getString(R.string.share_prior, localDate.format(dateTimeFormatter), titles[i], authors[i]);

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

}

