package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static DateTimeFormatter dateFormatInternal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter dateFormatToUser = DateTimeFormatter.ofPattern("MMM d");

    /* isRevealed checks if the Bookle has been revealed today. If so, returns true; false otherwise. */
    public static boolean isRevealed(@NonNull Context mContext) {
        SharedPreferences sharedPref = mContext
                .getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        String today = LocalDate.now().format(dateFormatInternal);
        String lastReveal = sharedPref.getString(mContext.getString(R.string.reveal), "1900-01-01");
        return (today.compareTo(lastReveal) == 0);
    }

    public static boolean hasGuessed(@NonNull Context mContext) {
        SharedPreferences sharedPref = mContext
                .getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        String today = LocalDate.now().format(dateFormatInternal);

        String lastReveal = sharedPref.getString(mContext.getString(R.string.last_guess_date), "");
        return (today.compareTo(lastReveal) == 0);
    }

    /* setDarkMode checks if user has chosen dark mode. If so, sets the page to dark mode. */
    public static void setDarkMode(@NonNull Context mContext) {
        SharedPreferences sharedPref = mContext
                .getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean darkmode = sharedPref.getBoolean(mContext.getString(R.string.darkmode), false);
        if (darkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void doFromDatabase(@NonNull DatabaseReference databaseReference,
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
