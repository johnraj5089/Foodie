package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email_edittext;
    //TextInputLayout password_layout, confirm_password_layout;
    TextInputEditText password_edittext, confirm_password_edittext;
    Button signup;
    TextView already_user;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email_edittext = findViewById(R.id.email_edittext1);
        password_edittext = findViewById(R.id.password_edittext1);
        confirm_password_edittext = findViewById(R.id.confirm_password_edittext);
        signup = findViewById(R.id.signup_btn1);
        already_user = findViewById(R.id.already_user);
        progressBar = findViewById(R.id.progressBar2);

        already_user.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signup.setOnClickListener(view -> {
            String email, password, confirm_password;
            email = email_edittext.getText().toString();
            password = password_edittext.getText().toString();
            confirm_password = confirm_password_edittext.getText().toString();

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
            }else if (password.length() < 6 || password.length() > 30) {
                String s = "Password must be 6 or more characters!";
                password_edittext.setError(s);
                password_edittext.requestFocus();
                return;
            }
            if (!confirm_password.equals(password)){
                String s = "Password doesn't match!";
                confirm_password_edittext.setError(s);
                confirm_password_edittext.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registration successful! Please verify your email.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.putExtra("showLoginMessage", true);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
        });
    }
}