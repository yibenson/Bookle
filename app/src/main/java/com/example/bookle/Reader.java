package com.example.bookle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.icu.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
//import android.icu.util.Calendar;
import java.time.LocalDate;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.bookle.databinding.EreaderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;


/* E-reader section. Credit for bottom sheet dialog goes to Joseph Chege's tutorial on section.io */

public class Reader extends AppCompatActivity {
    EreaderBinding ereaderBinding;
    SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ereaderBinding = EreaderBinding.inflate(getLayoutInflater());
        setContentView(ereaderBinding.getRoot());
        ereaderBinding.backButton.setOnClickListener(view -> close());
        ereaderBinding.appName.setOnClickListener(view -> close());

        ereaderBinding.optionsBttn.setOnClickListener(v -> showBottomSheetDialog());
        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean revealed = Utils.isRevealed(this);
        if (revealed) {
            ereaderBinding.reveal.setVisibility(View.GONE);
        } else {
            ereaderBinding.reveal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reveal(ereaderBinding.reveal);
                }
            });
        }

        /* Loads Reader with HTML rich text from Firebase */
        String day = getIntent().getStringExtra("DAY");
        loadText(day);
    }

    private void loadText(String day) {
        /* Sets textsize as recorded in Shared Preferences */
        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        float textsize = sharedPref.getFloat(getString(R.string.textsize), 20f);
        ereaderBinding.readerText.setTextSize(textsize);

        DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                .child("Books").child(day);

        Utils.databaseMethod actionText = (raw) ->
                ereaderBinding.readerText.setText(Html.fromHtml(raw, Html.FROM_HTML_MODE_LEGACY));
        Utils.doFromDatabase(databaseToday, "text", actionText);
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        /* Initializes dark mode switch with saved values and sets a mode-switch listener */
        SwitchCompat darkmodeswitch = bottomSheetDialog.findViewById(R.id.darkmode_switch);
        AtomicBoolean darkmode = new AtomicBoolean(sharedPref.getBoolean(getString(R.string.darkmode), false));
        darkmodeswitch.setChecked(darkmode.get());
        darkmodeswitch.setOnCheckedChangeListener((compoundButton, b) -> {
            darkmode.set(b);
            sharedPref.edit().putBoolean(getString(R.string.darkmode), darkmode.get()).apply();
            if (darkmode.get()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            bottomSheetDialog.dismiss();
        });

        /* Initializes textsize slider to saved value and sets textsize change listener */
        float textsize = sharedPref.getFloat(getString(R.string.textsize), 20f);
        Slider discreteSlider = bottomSheetDialog.findViewById(R.id.discreteSlider);
        float slidervalue = 100 - ((32f - textsize) * 5f);
        discreteSlider.setValue(slidervalue);

        discreteSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                // nothing necessary
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                float textsize = 32 - ((100 - slider.getValue())/5);
                ereaderBinding.readerText.setTextSize(textsize);
                sharedPref.edit().putFloat(getString(R.string.textsize), textsize).apply();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void close() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void reveal(View view) {
        String today = LocalDate.now().toString();
        sharedPref.edit().putString(getString(R.string.reveal), today).apply();
        startActivity(new Intent(getApplicationContext(), BookToday.class));
        finish();
    }

}
