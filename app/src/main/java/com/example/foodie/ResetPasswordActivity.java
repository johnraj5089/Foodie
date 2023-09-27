package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    Button reset;
    EditText email;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset = findViewById(R.id.reset_btn);
        email = findViewById(R.id.email_edittext);
        progressBar = findViewById(R.id.progressBar);


        reset.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = email.getText().toString();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ResetPasswordActivity.this, "Email sent! Reset password by email.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        });
    }
}