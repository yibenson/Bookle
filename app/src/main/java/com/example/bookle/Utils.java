package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import java.time.LocalDate;

public class Utils {
    public static boolean isRevealed(Context mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        String today = LocalDate.now().toString();
        String lastReveal = sharedPref.getString(mContext.getString(R.string.reveal), "");
        return today.equals(lastReveal);
    }

    public static void setDarkMode(Context mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean darkmode = sharedPref.getBoolean(mContext.getString(R.string.darkmode), false);
        if (darkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
