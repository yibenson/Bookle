package com.example.bookle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookle.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // authenticate();
        Utils.setDarkMode(this);
        setCover();

        Intent readerIntent = new Intent(getApplicationContext(), Reader.class);
        String today = LocalDate.now().format(dateTimeFormatter);
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

    private void setCover() {
        String today = LocalDate.now().format(dateTimeFormatter);
        boolean revealed = Utils.isRevealed(this);
        if (!revealed) {
            binding.cover.setBackground(getDrawable(R.drawable.mysterybook));
        } else {
            DatabaseReference databaseToday = FirebaseDatabase.getInstance().getReference()
                    .child("Books").child(today);

            Utils.databaseMethod action = (imageUri) ->
                    Picasso.get().load(imageUri).into(binding.cover);
            Utils.doFromDatabase(databaseToday, "cover", action);

        }
    }

}