package com.evillari.sip;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;



public class loginmain extends AppCompatActivity {



    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    TextView fbstatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_loginmain);

        fbstatus = (TextView)findViewById(R.id.tv_fb_status);

        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        fbLoginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {



            @Override
            public void onSuccess(LoginResult loginResult) {


                Intent intent = new Intent(getApplicationContext(), welcome.class);
                startActivity(intent);
                finish();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getApplicationContext(), "An Error has occured: " + error.toString(), Toast.LENGTH_SHORT).show();


            }
        });



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){

            String logout="logout";
            String register="register";
            if (logout.equals(bundle.getString("logout"))){

                fbstatus.setText("Logout successful.");

            } else if (register.equals(bundle.getString("register"))){

                fbstatus.setText("Please contact admin. You need to register your account.");

            } else {

                fbstatus.setText("Contact Admin to register your userID: " + bundle.getString("userID"));

            }

        } else {
            fbstatus.setText("");
        }


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }







}
