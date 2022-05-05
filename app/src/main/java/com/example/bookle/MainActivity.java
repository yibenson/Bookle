package com.example.bookle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bookle.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authenticate();
        setDarkMode();
        setCover();

        Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
        String today = LocalDate.now().toString();
        readerIntent.putExtra("DAY", today);

        binding.cover.setOnClickListener(view -> startActivity(readerIntent));

        binding.helpIcon.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Help.class)));

        binding.bookshelfIcon.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Bookshelf.class)));
    }

    private void authenticate() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(getApplicationContext(), "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                           // Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private void setCover() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean revealed = sharedPref.getBoolean(getString(R.string.reveal), false);
        if (!revealed) {
            binding.cover.setBackground(getDrawable(R.drawable.mysterybook));
        } else {
            binding.cover.setBackground(getDrawable(R.drawable.prideprejudice));
        }
    }

}