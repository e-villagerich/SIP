package com.evillari.sip;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class loginmain extends AppCompatActivity {


    private final static String TAG = "LOGGING";
    private FirebaseAuth mAuth;
    private EditText etUsername, etPassword;
    private Button btnLogin, btnLoginSignUp;
    private TextView loginstatus;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("UserProfile");

    private String srole = "test";


    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loginmain);
        mAuth = FirebaseAuth.getInstance();
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLoginSignUp = (Button)findViewById(R.id.btn_login_signup);
        loginstatus = (TextView)findViewById(R.id.tv_login_status);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginstatus.setText("");
                v.findViewById(R.id.btn_login).setEnabled(false);
                if((!etUsername.getText().toString().matches("")) && (!etPassword.getText().toString().matches(""))&&(etUsername.getText().toString().matches(emailPattern))){
                    signIn(etUsername.getText().toString(),etPassword.getText().toString());
                }
                else {
                    v.findViewById(R.id.btn_login).setEnabled(true);
                    loginstatus.setText("Please enter a valid username and password.");
                }


            }

        });


        btnLoginSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), signup.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String logout = "logout";
            String register = "register";
            if (logout.equals(bundle.getString("logout"))) {

            } else if (register.equals(bundle.getString("register"))) {


            } else {

            }

        } else {

        }

    }

    private void signIn(final String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //super user admin
                            if (email.equals("efrenvillarico@gmail.com")){
                                Intent intent = new Intent(getApplicationContext(), adminActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.d(TAG, "check user profile for " + email);

                                // TODO check if user profile existed and if not request admin to create it.

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        char[] chars = email.toCharArray();
                                        StringBuilder hex = new StringBuilder();
                                        for (char ch : chars) {
                                            hex.append(Integer.toHexString((int) ch));
                                        }
                                        if(dataSnapshot!=null) {
                                            if (dataSnapshot.hasChild(hex.toString())) {

                                                srole = dataSnapshot.child(hex.toString()).child("userrole").getValue().toString();

                                                switch (srole) {
                                                    case "admin":
                                                        Intent adminintent = new Intent(getApplicationContext(), adminActivity.class);
                                                        startActivity(adminintent);
                                                        finish();
                                                        break;
                                                    case "user":
                                                        Intent userintent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(userintent);
                                                        finish();
                                                        break;
                                                }

                                            } else {
                                                loginstatus.setText("You don't have any profile yet for " + email + ". Please contact admin.");
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }
                        else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {

                                loginstatus.setText("Invalid Crendentials.");

                            } catch(FirebaseAuthInvalidUserException e){

                                loginstatus.setText("User does not exist.");

                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }

                        }

                    }
                });



        btnLogin.setEnabled(true);

    }

}
