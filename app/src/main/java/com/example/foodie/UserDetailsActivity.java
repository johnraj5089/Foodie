package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserDetailsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    Member member;
    Button save;
    TextView email;

    EditText name, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        email = findViewById(R.id.email_displayed);

        name = findViewById(R.id.name_edittext);

        age = findViewById(R.id.age_edittext);

        save = findViewById(R.id.save);

        member = new Member();

        email.setText(user.getEmail());

        String userid = user.getUid();

        ref = FirebaseDatabase.getInstance("https://foodie-a2f04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Member");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {

                    String name_value =  dataSnapshot.child(userid).child("name").getValue(String.class);
                    Integer age_value =  dataSnapshot.child(userid).child("age").getValue(Integer.class);

                    Log.d("Firebase", "Retrieving the value from the firebase.");

                    name = findViewById(R.id.name_edittext);
                    age = findViewById(R.id.age_edittext);


                    name.setText(name_value);
                    assert age_value != null;
                    age.setText(age_value.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
                Log.d("Firebase", "Listener canceled.");
                Log.w("Firebase", "Listener canceled.", error.toException());
            }
        });

        save.setOnClickListener(v -> {

            int age_ = Integer.parseInt(age.getText().toString().trim());

            member.setName(name.getText().toString().trim());
            member.setAge(age_);
            member.setEmail(email.getText().toString());

            ref.child(user.getUid()).setValue(member)
                .addOnSuccessListener(aVoid -> {
                    Log.d("UserDetailsActivity", "onSuccess");
                    // Data was successfully written
                    Toast.makeText(UserDetailsActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UserDetailsActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                })
                        .addOnFailureListener(e -> {
                            Log.e("UserDetailsActivity", "onFailure: " + e.getMessage());
                            // Handle any errors here
                            Toast.makeText(UserDetailsActivity.this, "Failed to save details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
        });
    }
}