package com.example.quiztaker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setDrawer();
        setNavigationView();
        myDialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new QuizListFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            myDialog.dismiss();
        } else {
            popLogoutDialog();
        }
    }

    private void setDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    private void popLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Are you sure you want to log out?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
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

    private void changePopupWindow (int layoutID) {
        myDialog.setContentView(layoutID);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
