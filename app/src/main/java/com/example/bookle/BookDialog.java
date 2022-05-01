package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    int[] post_reveal_covers = new int[]{ R.drawable.cover1, R.drawable.hmart, R.drawable.becoming, R.drawable.midnightlibrary, R.drawable.sociopathnextdoor, R.drawable.lastgraduatejpg, R.drawable.candyhouse, R.drawable.sevenhusbands, R.drawable.parisapartment, R.drawable.betweentwokingdoms, R.drawable.remindersofhim, R.drawable.seaoftranquility, R.drawable.vanishinghalf, R.drawable.thegirlwhofellfromthesky};

    String[] authors = new String[]{"Delia Owens", "Michelle Zauner", "Michelle Obama", "Matt Haig", "Martha Stout", "Naomi Novik", "Jennifer Egan",
    "Taylor Jenkins Reid", "Lucy Foley", "Suleika Jaouad", "Colleen Hoover", "Emily St. John Mandel", "Brit Bennett", "Heidi W. Durrow"};

    String[] titles = new String[]{"Where the Crawdads Sing", "Crying in H Mart", "Becoming", "The Midnight Library", "The Sociopath Next Door", "The Graduate", "The Candy House",
    "The Seven Husbands of Evelyn Hugo", "The Paris Apartment", "Between Two Kingdoms", "Reminders of Him", "Sea of Tranquility", "The Vanishing Heir", "The Girl Who Fell From The Sky"};

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookDialogBinding = BookDialogBinding.inflate(getLayoutInflater());
        setContentView(bookDialogBinding.getRoot());
        int index = getIntent().getIntExtra("BOOK", 0);
        LocalDate localDate = LocalDate.now().minusDays(index);
        bookDialogBinding.book1Cover.setBackground(AppCompatResources.getDrawable(this, post_reveal_covers[index]));
        bookDialogBinding.book1Author.setText("By " + authors[index]);
        bookDialogBinding.book1Title.setText(titles[index]);
        String text = "The Bookle on " + localDate.format(dateTimeFormatter) + " was...";
        bookDialogBinding.bookleMsg.setText(text);
        bookDialogBinding.bookleMsg.bringToFront();
        bookDialogBinding.backButton.setOnClickListener(view -> finish());
        setDarkMode();
    }

    public void setDarkMode() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean darkmode = sharedPref.getBoolean(getString(R.string.darkmode), false);
        if (darkmode) {
            Log.e("Bookshelf", "Attempting to set dark mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

