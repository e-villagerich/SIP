package com.evillari.sip;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import UI.Addstock;
import UI.Dashboard;
import UI.Logout;
import UI.Messenger;
import UI.Sellice;
import Utilities.CacheUtil;

public class MainActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_email;

    private ImageView profilepic;

    private final String TAG = "app_info:";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        navigationView = (NavigationView) findViewById(R.id.nav_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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


        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_dashboard:
                        Log.i(TAG, "home selected");
                        Dashboard dashboard = new Dashboard();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, dashboard)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_sellice:
                        Log.i(TAG, "sellice selected");
                        Sellice sellice = new Sellice();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, sellice)
                                .commit();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_addstock:
                        Log.i(TAG, "addstock selected");

                        Addstock addstock = new Addstock();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, addstock)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_messenger:
                        Log.i(TAG, "messenger selected");

                        Messenger messenger = new Messenger();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, messenger)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_about:
                        Log.i(TAG, "about selected");

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.menu_logout:
                        Log.i(TAG, "logout selected");

                        Logout logout = new Logout();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, logout)
                                .commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return onNavigationItemSelected(item);

                }

            }
        });


        Dashboard dashboard = new Dashboard();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, dashboard)
                .commit();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        displayProfile(currentUser);

    }




    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean draweropen = drawerLayout.isDrawerOpen(GravityCompat.START);
        menu.findItem(R.id.content_frame).setVisible(!draweropen);
        return onPrepareOptionsMenu(menu);

    }

    private void displayProfile(FirebaseUser currentUser) {


        tv_email.setText(currentUser.getEmail());
        tv_name.setText(currentUser.getDisplayName());
        Uri uri = currentUser.getPhotoUrl();
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(10)
                .oval(true)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        Picasso.with(MainActivity.this)
                .load(uri)
                .transform(transformation)
                .into(profilepic);
    }



}
