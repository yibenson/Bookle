package com.example.bookle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.bookle.databinding.EreaderBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.atomic.AtomicBoolean;


/* E-reader section. Credit for bottom sheet dialog goes to Joseph Chege's tutorial on section.io */

public class Reader extends AppCompatActivity {
    EreaderBinding ereaderBinding;
    SharedPreferences sharedPref;

    static final int MAX_GUESSES = 6;
    static ArrayList<String> guesses = new ArrayList<>();
    final String[] answer = new String[1];
    int[] guess_views = new int[]{R.id.guess1, R.id.guess2,R.id.guess3,
            R.id.guess4,R.id.guess5,R.id.guess6};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ereaderBinding = EreaderBinding.inflate(getLayoutInflater());
        setContentView(ereaderBinding.getRoot());
        ereaderBinding.backButton.setOnClickListener(view -> close());
        ereaderBinding.appName.setOnClickListener(view -> close());

        ereaderBinding.optionsBttn.setOnClickListener(v -> showBottomSheetDialog());
        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean revealed = Utils.isRevealed(this);
        if (revealed || pastDate()) {
            ereaderBinding.reveal.setVisibility(View.GONE);
        } else {
            ereaderBinding.reveal.setOnClickListener(view -> show_guess_dialog());
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
        Utils.databaseMethod actionTitle = string -> answer[0] = string;
        Utils.doFromDatabase(databaseToday, "title", actionTitle);
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
        if (!getIntent().hasExtra("SOURCE")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void reveal(View view) {
        String today = LocalDate.now().toString();
        sharedPref.edit().putString(getString(R.string.reveal), today).apply();

        startActivity(new Intent(getApplicationContext(), BookToday.class));
        finish();
    }

    private void show_guess_dialog() {
        Log.e("Reader", guesses.toString());
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = inflater.inflate(R.layout.guess_dialog, null);
        builder.setView(v)
                .setTitle(getString(R.string.guess_prompt))
                .setPositiveButton(R.string.reveal, (dialogInterface, i) -> reveal(null))
                .setNeutralButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setNegativeButton(R.string.guess, null);
        AlertDialog alert = builder.create();
        alert.setOnShowListener(dialogInterface -> {
            Button button = ((AlertDialog) alert).getButton(AlertDialog.BUTTON_NEGATIVE);
            button.setOnClickListener(view -> { guess(v); });
        });

        setGuesses(v);
        alert.show();
    }

    public void guess(View view) {
        sharedPref.edit().putString(getString(R.string.last_guess_date), LocalDate.now()
                .format(Utils.dateFormatInternal)).apply();
        if (guesses.size() >= MAX_GUESSES) { return; }

        TextInputLayout guess_view = (TextInputLayout) view.findViewById(guess_views[guesses.size()]);
        String guess = guess_view.getEditText().getText().toString();
        guesses.add(guess);
        if (!checkGuess(guess)) {
            guess_view.setError(null);
            Toast.makeText(this, "Incorrect guess!", Toast.LENGTH_SHORT).show();
            disableEditTexts(view);
        } else {
            Toast.makeText(this, "Correct guess!", Toast.LENGTH_SHORT).show();
            sharedPref.edit().putBoolean(getString(R.string.correct_guess), true).apply();
            disableAllEditTexts(view);
            reveal(null);
        }

        saveArrayList(guesses, getString(R.string.guess));
    }

    private void setGuesses(View v) {
        if (!Utils.hasGuessed(this)) {
            guesses = new ArrayList<>();
            disableEditTexts(v);
        } else {
            guesses = getArrayList(getString(R.string.guess));
            if (sharedPref.getBoolean(getString(R.string.correct_guess), false)) {
                disableAllEditTexts(v);
            } else {
                disableEditTexts(v);
            }
        }
    }

    private boolean checkGuess(String s) {
        return (s.compareTo(answer[0]) == 0);
    }

    private void disableEditTexts(View root) {
        EditText e;
        for (int i = 0; i < MAX_GUESSES; i++) {
            e = ((TextInputLayout) root.findViewById(guess_views[i])).getEditText();
            if (i == guesses.size()) {
                e.setFocusable(true);
                e.setFocusableInTouchMode(true);
                e.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                if (i < guesses.size()) {
                    e.setText(guesses.get(i));
                }
                e.setFocusable(false);
                e.setFocusableInTouchMode(false);
                e.setInputType(InputType.TYPE_NULL);
            }
        }
    }

    private void disableAllEditTexts(View root) {
        EditText e;
        for (int i = 0; i < MAX_GUESSES; i++) {
            e = ((TextInputLayout) root.findViewById(guess_views[i])).getEditText();
            if (i < guesses.size()) {
                e.setText(guesses.get(i));
            }
            e.setFocusable(false);
            e.setFocusableInTouchMode(false);
            e.setInputType(InputType.TYPE_NULL);
        }
    }

    public void saveArrayList(ArrayList<String> list, String key){
        Gson gson = new Gson();
        String jsonList = gson.toJson(list);
        sharedPref.edit().putString(key, jsonList).apply();
    }

    public ArrayList<String> getArrayList(String key){
        Gson gson = new Gson();
        String json = sharedPref.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private boolean pastDate() {
        return getIntent().getStringExtra("DAY").compareTo(LocalDate.now()
                .format(Utils.dateFormatInternal)) != 0;
    }






}
