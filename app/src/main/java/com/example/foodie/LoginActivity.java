package com.example.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    EditText email_edittext;
    TextInputEditText password_edittext;
    TextInputLayout password_layout;
    Button login, signup;

    TextView forgot_password;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login = findViewById(R.id.login_btn);
        forgot_password = findViewById(R.id.forgot_password);
        signup = findViewById(R.id.reset_btn);
        progressBar = findViewById(R.id.progressBar);
        password_layout = findViewById(R.id.password_layout);
        //Drawable eyeIcon = ContextCompat.getDrawable(this, R.drawable.baseline_eye_open);

        forgot_password.setOnClickListener(v -> {
            Intent in = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(in);
            finish();
        });

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://foodie-a2f04-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference reference = database.getReference("Member");

        login.setOnClickListener(view -> {
            String email, password;
            email = email_edittext.getText().toString();
            password = Objects.requireNonNull(password_edittext.getText()).toString();

            if (email.isEmpty()){
                String s = "Email is empty!";
                email_edittext.setError(s);
                email_edittext.requestFocus();
                return;
            }

            if (password.isEmpty()){
                String s = "Password is empty!";
                password_edittext.setError(s);
                password_edittext.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (mAuth.getCurrentUser() != null){
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Login successful.",
                                        Toast.LENGTH_SHORT).show();

                                Log.d("Firebase", mAuth.getCurrentUser().getUid());

                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()) {

                                            Log.d("Firebase", "Checking the existence");
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {

                                            Intent i = new Intent(LoginActivity.this, UserDetailsActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle any errors here
                                        Log.d("Firebase", "Listener canceled.");
                                        Log.w("Firebase", "Listener canceled.", error.toException());
                                    }
                                });

                            } else {
                                Toast.makeText(LoginActivity.this, "Credentials invalid or verify your email to login.",
                                        Toast.LENGTH_SHORT).show();
                                //mAuth.signOut();
                            }
                        }else  {
                            Toast.makeText(LoginActivity.this, "Authentication Failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
