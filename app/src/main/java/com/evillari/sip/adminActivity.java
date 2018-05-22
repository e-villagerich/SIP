package com.evillari.sip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;



import java.net.URL;
import java.util.List;

import UI.AdminDashboard;
import UI.CreateProfile;
import UI.Logout;
import UI.Messenger;

public class adminActivity extends AppCompatActivity {

    private String Args[]=new String[10];
    private String token;
    private String username;

    private ImageView profilepic;
    private TextView tv_email;
    private TextView tv_name;

    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private final String TAG = "app_info:";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final Intent intent = getIntent();
        String result[] = intent.getStringArrayExtra("result");



        navigationView = (NavigationView) findViewById(R.id.admin_navmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.admin_drawerlayout);
        fragmentManager = getSupportFragmentManager();


        View viewHeader = navigationView.getHeaderView(0);
        profilepic = (ImageView)viewHeader.findViewById(R.id.iv_pic_header);
        tv_name = (TextView)viewHeader.findViewById(R.id.header_name);
        tv_email = (TextView)viewHeader.findViewById(R.id.header_email);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_dashboard_admin:

                        AdminDashboard adminDashboard = new AdminDashboard();
                        fragmentManager.beginTransaction()
                                .replace(R.id.admin_content_frame, adminDashboard)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_messenger_admin:
                        Messenger messenger = new Messenger();
                        fragmentManager.beginTransaction()
                                .replace(R.id.admin_content_frame,messenger)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.menu_create_profile_admin:
                        CreateProfile createProfile = new CreateProfile();
                        fragmentManager.beginTransaction()
                                .replace(R.id.admin_content_frame, createProfile)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_about_admin:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_logout_admin:
                        Logout logout = new Logout();
                        fragmentManager.beginTransaction()
                                .replace(R.id.admin_content_frame,logout)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return onNavigationItemSelected(item);

                }
            }
        });


        AdminDashboard adminDashboard = new AdminDashboard();
        fragmentManager.beginTransaction()
                .add(R.id.admin_content_frame,adminDashboard)
                .commit();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean draweropen = drawerLayout.isDrawerOpen(GravityCompat.START);
        menu.findItem(R.id.admin_content_frame).setVisible(!draweropen);
        return onPrepareOptionsMenu(menu);

    }


    private void displayProfile(FirebaseUser currentUser) {

            tv_email.setText(currentUser.getEmail());
            tv_name.setText(currentUser.getDisplayName());
            Uri uri = currentUser.getPhotoUrl();
            Log.i(TAG, uri.toString());
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(10)
                    .oval(true)
                    .scaleType(ImageView.ScaleType.CENTER_CROP)
                    .build();
            Picasso.with(adminActivity.this)
                    .load(uri)
                    .transform(transformation)
                    .into(profilepic);

    }

}
