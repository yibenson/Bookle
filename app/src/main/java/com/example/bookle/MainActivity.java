package com.example.bookle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bookle.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseReference myRef;
    String i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Reader.class));

                /*
                String username = binding.username.getText().toString();
                String name = binding.name.getText().toString();
                String lastname = binding.lastname.getText().toString();
                String age = binding.age.getText().toString();
                Log.v("Firebase", "Attempting to connect");
                updateData(name, lastname, age);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String i = (String) dataSnapshot.getValue();
                        Log.v("Firebase", i);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                // Write a message to the database
                // myRef.setValue("New value will be: Hello world 1");

                 */
            }
        });


        // Read from the database
        /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Firebase", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

         */
    }

    private void updateData(String name, String lastname, String age) {
        HashMap User = new HashMap();
        User.put("firstName", name);
        User.put("lastName", lastname);
        User.put("age", age);
        myRef = FirebaseDatabase.getInstance().getReference("Users");

    }


}