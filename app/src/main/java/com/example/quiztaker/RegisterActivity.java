package com.example.quiztaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private TextInputLayout emailInput;
    private TextInputLayout firstNameInput;
    private TextInputLayout lastNameInput;
    private TextInputLayout passwordInput;
    private TextInputLayout confirmPasswordInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.emailInput = findViewById(R.id.email);
        this.firstNameInput = findViewById(R.id.firstname);
        this.lastNameInput = findViewById(R.id.lastname);
        this.passwordInput = findViewById(R.id.password);
        this.confirmPasswordInput = findViewById(R.id.confirm_password);

        this.progressBar = findViewById(R.id.progress_bar);
        this.progressBar.setVisibility(ProgressBar.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user = setUserNames(user);
                    navigateToHome();

                } else {
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });
    }

    private FirebaseUser setUserNames(FirebaseUser user) {
        String firstName = firstNameInput.getEditText().getText().toString();
        String lastName = lastNameInput.getEditText().getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + " " + lastName)
                .build();
        user.updateProfile(profileUpdates)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void validateInputs(View view) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        if (!validateEmail() || !validateNames() || !validatePasswords()) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            return;
        }

        createAccount(
                this.emailInput.getEditText().getText().toString(),
                this.passwordInput.getEditText().getText().toString()
        );
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

    private boolean validateNames() {
        String firstName = this.firstNameInput.getEditText().getText().toString();
        String lastName = this.lastNameInput.getEditText().getText().toString();
        if(firstName.isEmpty()) {
            this.firstNameInput.setError("First name cannot be empty");
            return false;
        }

        if(lastName.isEmpty()) {
            this.lastNameInput.setError("Last name cannot be empty");
            return false;
        }

        this.firstNameInput.setError(null);
        this.lastNameInput.setError(null);
        return true;
    }

    private boolean validatePasswords() {
        String password = this.passwordInput.getEditText().getText().toString();
        String confirmPassword = this.confirmPasswordInput.getEditText().getText().toString();
        if(password.isEmpty()) {
            this.passwordInput.setError("Password cannot be empty");
            return false;
        }

        if(password.length() < 6) {
            this.passwordInput.setError("Password must be at least 6 characters long");
            return false;
        }

        if(!password.equals(confirmPassword)) {
            this.passwordInput.setError("Passwords do not match");
            this.confirmPasswordInput.setError("Passwords do not match");
            return false;
        }

        this.passwordInput.setError(null);
        this.confirmPasswordInput.setError(null);
        return true;
    }
}
