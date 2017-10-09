package com.evillari.sip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import Utilities.CacheUtil;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CacheUtil cu = new CacheUtil();
        String sessioninfo[]= new String[8];

        if((AccessToken.getCurrentAccessToken()!=null) && (cu.sessionStatus(this))) {

            sessioninfo = cu.getSessioninfo(this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("result", sessioninfo);
            startActivity(intent);
            finish();


        } else if((AccessToken.getCurrentAccessToken()!=null) && (cu.adminSessionStatus(this))) {


            Intent intent = new Intent(this, adminActivity.class);
            startActivity(intent);
            finish();


        } else {

            if(AccessToken.getCurrentAccessToken()!= null){
                LoginManager.getInstance().logOut();
            }

            Intent intent = new Intent(this, loginmain.class);
            startActivity(intent);
            finish();
        }

    }
}
