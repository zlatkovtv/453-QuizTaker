package com.example.quiztaker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        // Check if user is exists, and if so, redirect to home
        if(loggedUser != null) {
            navigateToHome();
        }

        this.emailInput = findViewById(R.id.email);
        this.passwordInput = findViewById(R.id.password);
        this.progressBar = findViewById(R.id.progress_bar);
        this.progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void validateInput(View view) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        String email = emailInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        if (!validateEmail() || !validatePassword()) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            return;
        }

        signIn(email, password);
    }

    private boolean validateEmail() {
        String email = this.emailInput.getEditText().getText().toString();
        if(email.isEmpty()) {
            this.emailInput.setError("Email cannot be empty");
            return false;
        }

        if(!email.contains("@")) {
            this.emailInput.setError("Email not in correct format");
            return false;
        }

        this.emailInput.setError(null);
        return true;
    }

    private boolean validatePassword() {
        String password = this.passwordInput.getEditText().getText().toString();
        if(password.isEmpty()) {
            this.passwordInput.setError("Password cannot be empty");
            return false;
        }

        this.passwordInput.setError(null);
        return true;
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    navigateToHome();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void navigateToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
