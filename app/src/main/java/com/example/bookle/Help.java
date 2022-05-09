package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bookle.databinding.ActivityHelpBinding;
import com.example.bookle.databinding.EreaderBinding;

public class Help extends AppCompatActivity {
    ActivityHelpBinding helpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helpBinding = helpBinding.inflate(getLayoutInflater());
        setContentView(helpBinding.getRoot());
        helpBinding.helpClose.setOnClickListener(view -> finish());

        Utils.setDarkMode(this);
    }
}