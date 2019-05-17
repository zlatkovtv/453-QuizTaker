package com.example.quiztaker;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment to change profile details, such as names and profile picture.
 * Accessible from drawer
 */
public class ChangeProfileFragment extends Fragment {

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String mUri;
    private StorageTask uploadTask;

    DocumentReference documentReference;
    FirebaseUser user;

    public ImageView profilePhoto;
    public EditText firstName, lastName;
    public Button buttonConfirm, buttonCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        profilePhoto = view.findViewById(R.id.imageView_changeProfile);
        firstName = view.findViewById(R.id.editText_changeFirstName);
        lastName = view.findViewById(R.id.editText_changeLastName);
        buttonConfirm = view.findViewById(R.id.Button_changeProfileConfirm);
        buttonCancel = view.findViewById(R.id.Button_changeProfileCancel);

        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadProfilePhoto();

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageFromCamera();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (!firstName.getText().toString().equals("")) {
                        documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("firstName", firstName.getText().toString());
                        documentReference.update(map);

                        refreshActivity();
                    }
                    if (!lastName.getText().toString().equals("")) {
                        documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("lastName", lastName.getText().toString());
                        documentReference.update(map);

                        refreshActivity();
                    }
                    if (mUri != null) {
                        documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        documentReference.update(map);

                        refreshActivity();
                    }
                    if (firstName.getText().toString().equals("") && lastName.getText().toString().equals("") && mUri == null) {
                        Toast.makeText(getContext(), "Nothing to update", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                } else {
                    Toast.makeText(getContext(), "You are not logged in", Toast.LENGTH_SHORT).show();
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

    /**
     * Reloads the current Activity to update the views holding profile data
     */
    private void refreshActivity() {
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    /**
     * loads the imageURL from the database of the current user into the ImageView, profilePhoto
     */
    private void loadProfilePhoto() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userID = firebaseFirestore.collection("Users").document(user.getUid());
        userID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot profileData = task.getResult();
                    if (profileData != null) {
                        if (profileData.getString("imageURL").equals("default"))
                            profilePhoto.setImageResource(R.drawable.profile_icon);
                        else {
                            Glide.with(getContext()).load(profileData.getString("imageURL")).into(profilePhoto);
                        }
                    }
                }
            }
        });
    }

    /**
     * Opens up the camera roll to select an image
     */
    private void loadImageFromCamera() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    /**
     * Gets the file extension of the Uri of a selected image passed into this method
     * @param uri The uri of a selected image
     * @return The file extension of the selected image (eg: .png, .jpeg, etc)
     */
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * Uploads the selected image from Camera Roll to Firebase Storage
     */
    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() +"." +getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();

                        //delete previous user image from firebase storage
                        documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot profileData = task.getResult();
                                    if (!profileData.getString("imageURL").equals("default")) {
                                        StorageReference previousPhoto = FirebaseStorage.getInstance().getReferenceFromUrl(profileData.getString("imageURL"));
                                        previousPhoto.delete();
                                    }
                                }
                            }
                        });

                        Toast.makeText(getContext(), "SUCCESS: Image Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getContext(), "FAILED: Could not update profile pic", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method after image is selected from Camera Roll
     * @param requestCode The type of request we're responding to
     * @param resultCode If the operation successful or not
     * @param data Data of the image selected
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "uploading", Toast.LENGTH_SHORT).show();
            } else {
                profilePhoto.setImageURI(imageUri);
                uploadImage();
            }
        }
    }
}
