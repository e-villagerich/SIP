package com.evillari.sip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Utilities.CacheUtil;

public class splash extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CacheUtil cu = new CacheUtil();
        String sessioninfo[]= new String[8];

        if((currentUser!=null) && (cu.sessionStatus(this))) {

            sessioninfo = cu.getSessioninfo(this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("result", sessioninfo);
            startActivity(intent);
            finish();


        } else if((currentUser!=null) && (cu.adminSessionStatus(this))) {


            sessioninfo = cu.getAdminSessioninfo(this);
            Intent intent = new Intent(this, adminActivity.class);
            intent.putExtra("result", sessioninfo);
            startActivity(intent);
            finish();


        } else {

            if(currentUser!= null){

            }

            Intent intent = new Intent(this, loginmain.class);
            startActivity(intent);
            finish();
        }

    }
}
