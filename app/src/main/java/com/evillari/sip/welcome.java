package com.evillari.sip;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Utilities.CacheUtil;
import Utilities.JsonUtils;
import Utilities.NetworkUtils;
import Utilities.REQUEST;

public class welcome extends AppCompatActivity {

    private String ARGS[] = new String[10];
    private String date="01/01/1980";
    private NetworkUtils.checkConnectivity nu;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    private static final String TAG = "SIP.welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

    }
}
