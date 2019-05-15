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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private final boolean IS_ADMIN = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private TextInputLayout emailInput;
    private TextInputLayout firstNameInput;
    private TextInputLayout lastNameInput;
    private TextInputLayout passwordInput;
    private TextInputLayout confirmPasswordInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getViews();
        this.progressBar.setVisibility(ProgressBar.INVISIBLE);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Gets views needed in this activity
     */
    private void getViews() {
        this.emailInput = findViewById(R.id.email);
        this.firstNameInput = findViewById(R.id.firstname);
        this.lastNameInput = findViewById(R.id.lastname);
        this.passwordInput = findViewById(R.id.password);
        this.confirmPasswordInput = findViewById(R.id.confirm_password);
        this.progressBar = findViewById(R.id.progress_bar);
    }

    /**
     * Creates a new FirebaseUser and logs in
     * @param email The validated email
     * @param password The validated password
     */
    public void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    setUserNames(user);
                    addToDatabase(user);
                    setUserRole(user);
                    navigateToHome();
                } else {
                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });
    }

    private void addToDatabase(FirebaseUser user) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", firstNameInput.getEditText().getText().toString());
        map.put("lastName", lastNameInput.getEditText().getText().toString());
        map.put("imageURL", "default");
        map.put("isAdmin", true);
        firebaseFirestore.collection("Users").document(user.getUid()).set(map);
    }

    private void setUserNames(FirebaseUser user) {
        /**
         * Sets Firstname and Lastname of the FirebaseUser
         * @param firstName
         * @param lastName
         */

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
    }

    /**
     * Creates a document in the Users collection with the "isAdmin" field
     * @param user
     */
    private void setUserRole(FirebaseUser user) {
        String userId = user.getUid();
        Map<String, Object> userRole = new HashMap<>();
        userRole.put("isAdmin", IS_ADMIN);
        db.collection("Users").document(userId).set(userRole);
    }

    /**
     * Navigates to the Home Activity
     */
    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    /**
     * Validates email, names and passwords and logs in
     * @param view
     */
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

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
