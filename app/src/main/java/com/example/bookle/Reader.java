package com.example.bookle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.bookle.databinding.EreaderBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import java.util.concurrent.atomic.AtomicBoolean;

/* E-reader section. Credit for bottom sheet dialog goes to Joseph Chege's tutorial on section.io */

public class Reader extends AppCompatActivity {
    EreaderBinding ereaderBinding;
    SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ereaderBinding = EreaderBinding.inflate(getLayoutInflater());
        setContentView(ereaderBinding.getRoot());
        ereaderBinding.optionsBttn.setOnClickListener(v -> showBottomSheetDialog());
        ereaderBinding.backButton.setOnClickListener(view -> finish());

        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        float textsize = sharedPref.getFloat(getString(R.string.textsize), 20f);
        ereaderBinding.readerText.setTextSize(textsize);

    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
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

        Slider discreteSlider = bottomSheetDialog.findViewById(R.id.discreteSlider);
        float textsize = sharedPref.getFloat(getString(R.string.textsize), 20f);
        ereaderBinding.readerText.setTextSize(textsize);
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


}
