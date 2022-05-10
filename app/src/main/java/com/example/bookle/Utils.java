package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    /* isRevealed checks if the Bookle has been revealed today. If so, returns true; false otherwise. */
    public static boolean isRevealed(Context mContext) {
        SharedPreferences sharedPref = mContext
                .getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(dateTimeFormatter);
        String lastReveal = sharedPref.getString(mContext.getString(R.string.reveal), "1900-01-01");
        return (today.compareTo(lastReveal) == 0);
    }

    public static boolean hasGuessed(Context mContext) {
        SharedPreferences sharedPref = mContext
                .getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(dateTimeFormatter);

        String lastReveal = sharedPref.getString(mContext.getString(R.string.last_guess_date), "");
        return (today.compareTo(lastReveal) == 0);
    }

    /* setDarkMode checks if user has chosen dark mode. If so, sets the page to dark mode. */
    public static void setDarkMode(Context mContext) {
        SharedPreferences sharedPref = mContext
                .getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean darkmode = sharedPref.getBoolean(mContext.getString(R.string.darkmode), false);
        if (darkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void doFromDatabase(DatabaseReference databaseReference,
                                      String key, databaseMethod action) {
        databaseReference.child(key).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                String msg = String.format("Error getting %s data", key);
                Log.e("firebase", msg, task.getException());
            }
            else {
                String raw = String.valueOf(task.getResult().getValue());
                action.method(raw);
            }
        });
    }

    @FunctionalInterface
    public interface databaseMethod {
        void method(String string);
    }

}
