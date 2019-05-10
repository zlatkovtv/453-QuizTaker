package com.example.quiztaker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    EditText newPassword, confirmNewPassword;
    Button buttonChangePassword;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        //oldPassword = view.findViewById(R.id.EditText_oldPassword);
        newPassword = view.findViewById(R.id.EditText_newPassword);
        confirmNewPassword = view.findViewById(R.id.EditText_confirmNewPassword);
        buttonChangePassword = view.findViewById(R.id.Button_changePassword);
        //email = view.findViewById(R.id.email);
        dialog = new ProgressDialog(view.getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (user != null)
                {
                    //if (!email.getText().toString().equals(user.getEmail()))
                        //Toast.makeText(view.getContext(), "Incorrrect Email.", Toast.LENGTH_SHORT).show();
                    if (newPassword.getText().toString().equals("") || confirmNewPassword.getText().toString().equals(""))
                        Toast.makeText(view.getContext(), "Missing field.", Toast.LENGTH_SHORT).show();
                    else if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString()))
                        Toast.makeText(view.getContext(), "Passwords Do Not Match.", Toast.LENGTH_SHORT).show();
                    else {
                        dialog.setMessage("Changing password, please wait");
                        dialog.show();
                        user.updatePassword(confirmNewPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword.getText().toString());
                                        //user.reauthenticate(credential);
                                        if (task.isSuccessful()){
                                            dialog.dismiss();
                                            Toast.makeText(view.getContext(), "Your password has been changed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            dialog.dismiss();
                                            Log.e("TASK UNSUCCESSFUL", task.getException().toString());
                                            Toast.makeText(view.getContext(), "Password could not be changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}
