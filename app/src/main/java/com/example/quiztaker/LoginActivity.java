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

/**
 * Activity for existing user login
 */
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if(loggedUser != null) {
            navigateToHome();
        }

        getViews();
        this.progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    /**
     * Gets views needed within the activity
     */
    private void getViews() {
        this.emailInput = findViewById(R.id.email);
        this.passwordInput = findViewById(R.id.password);
        this.progressBar = findViewById(R.id.progress_bar);
    }

    /**
     * Validates email and password and signs in
     * @param view
     */
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

    /**
     * Signs in the FirebaseUser
     * @param email The validated email of the user
     * @param password The validated password of the user
     */
    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    navigateToHome();
                    return;
                }

                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    /**
     * Starts the Home Activity
     */
    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    /**
     * Starts the Registeractivity
     * @param view
     */
    public void navigateToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
