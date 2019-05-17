package com.example.quiztaker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

/**
 * The home activity is what the user sees when they login. Serves for inflating fragments in it.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {
    private DrawerLayout drawer;
    private Dialog myDialog;
    private ImageView profilePhoto;
    private String imageURL;
    private TextView fistName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupDrawer();
        setupNavigationView();
        getProfileFromDatabase();
        myDialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        myDialog.setOnDismissListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new QuizListFragment()).commit();
        }
    }

    /**
     * Fetches profile data from database for the current user
     */
    public void getProfileFromDatabase() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.getString("imageURL").equals("default"))
                            profilePhoto.setImageResource(R.drawable.profile_icon);
                        else {
                            imageURL = document.getString("imageURL");
                            Glide.with(HomeActivity.this).load(imageURL).into(profilePhoto);
                        }
                        fistName.setText(document.getString("firstName"));
                        lastName.setText(document.getString("lastName"));
                    }
                }
            }
        });
    }

    /**
     * This method will be invoked when the dialog is dismissed
     * @param dialog The dialog that was dismissed will be passed into the method
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.changeProfileFragment) != null) {
            ft.remove(getSupportFragmentManager().findFragmentById(R.id.changeProfileFragment));
            ft.commit();
        }
        if (getSupportFragmentManager().findFragmentById(R.id.changePasswordFragment) != null) {
            ft.remove(getSupportFragmentManager().findFragmentById(R.id.changePasswordFragment));
            ft.commit();
        }
    }

    /**
     * Tell all fragments to invoke their onBackPressed method
     * @return If at least one fragment handles the event, return true, else false
     */
    private boolean tellFragments(){
        boolean handled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof BaseFragment)
                handled = ((BaseFragment)f).onBackPressed();
            if(handled) {
                break;
            }
        }
        return handled;
    }

    /**
     * Passes on to active fragment. If event is not handled there, executes this logic
     */
    @Override
    public void onBackPressed() {
        boolean handled = tellFragments();
        if(handled) {
            return;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            myDialog.dismiss();
        }
        else if (myDialog.isShowing()){
            myDialog.dismiss();
        }
        else {
            popLogoutDialog();
        }
    }

    public BaseFragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * Sets up the App Drawer
     */
    private void setupDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Sets up the Navigation View for the App Drawer
     */
    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        profilePhoto = headerView.findViewById(R.id.profilePhoto);
        fistName = headerView.findViewById(R.id.textView_firstName);
        lastName = headerView.findViewById(R.id.textView_lastName);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeProfileButton:
                //open dialog for change profile
                changePopupWindow(R.layout.change_profile_popup);
                break;
            case R.id.changePasswordButton:
                //open dialog for change password
                changePopupWindow(R.layout.change_password_popup);
                break;
            case R.id.helpButton:
                //open dialog for help
                changePopupWindow(R.layout.help_popup);
                break;
            case R.id.aboutButton:
                //open dialog for about
                changePopupWindow(R.layout.about_popup);
                break;
            case R.id.logoutButton:
                popLogoutDialog();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Pops a confirmation dialog before logging out.
     */
    private void popLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Are you sure you want to log out?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }

            })
            .setNegativeButton("No", null)
            .show();
    }

    /**
     * Sets the layout to the dialog view
     * @param layoutID The ID of the layout to be set
     */
    private void changePopupWindow (int layoutID) {
        myDialog.setContentView(layoutID);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


}
