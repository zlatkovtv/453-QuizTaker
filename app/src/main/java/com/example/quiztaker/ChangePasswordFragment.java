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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Fragment to change password, accessible from the drawer
 */
public class ChangePasswordFragment extends Fragment {

    EditText newPassword, confirmNewPassword, currentPassword;
    Button buttonChangePassword, buttonCancel;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Toast.makeText(view.getContext(), "DEBUGGING", Toast.LENGTH_SHORT).show();
        currentPassword = view.findViewById(R.id.EditText_currentPassword);
        newPassword = view.findViewById(R.id.EditText_newPassword);
        confirmNewPassword = view.findViewById(R.id.EditText_confirmNewPassword);
        buttonChangePassword = view.findViewById(R.id.Button_changePassword);
        buttonCancel = view.findViewById(R.id.Button_changePasswordCancel);
        dialog = new ProgressDialog(view.getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (user != null)
                {
                    if (!currentPassword.getText().toString().equals("")) {
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword.getText().toString());
                        user.reauthenticate(credential);
                    }

                    if (newPassword.getText().toString().equals("") || confirmNewPassword.getText().toString().equals("") || currentPassword.getText().toString().equals(""))
                        Toast.makeText(view.getContext(), "Missing field/s.", Toast.LENGTH_SHORT).show();
                    else if (currentPassword.getText().toString().equals(newPassword.getText().toString()))
                        Toast.makeText(view.getContext(), "Cannot update to the same password.", Toast.LENGTH_SHORT).show();
                    else if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString()))
                        Toast.makeText(view.getContext(), "Passwords Do Not Match.", Toast.LENGTH_SHORT).show();
                    else {
                        dialog.setMessage("Changing password, please wait");
                        dialog.show();
                        user.updatePassword(confirmNewPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.dismiss();
                                            getActivity().onBackPressed();
                                            Toast.makeText(view.getContext(), "Your password has been changed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            dialog.dismiss();
                                            Log.e("TASK UNSUCCESSFUL", task.getException().toString());
                                            Toast.makeText(view.getContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
