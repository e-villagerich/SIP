package com.evillari.sip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    private Button btnsignup, btnlogin;
    private EditText signupemail, signuppw1, signuppw2;
    private TextView signupmsg;
    FirebaseAuth mAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("UserProfile");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        btnlogin = (Button)findViewById(R.id.btn_signup_login);
        btnsignup = (Button)findViewById(R.id.btn_signup);

        signupemail = (EditText)findViewById(R.id.et_signup_email);
        signuppw1 = (EditText)findViewById(R.id.et_signup_pw1);
        signuppw2 = (EditText)findViewById(R.id.et_signup_pw2);

        signupmsg = (TextView)findViewById(R.id.tv_signup_message);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), loginmain.class);
                startActivity(intent);
                finish();
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupmsg.setText("");
                if(!signupemail.getText().toString().matches("") && signupemail.getText().toString().matches(emailPattern)){
                    if(!signuppw1.getText().toString().matches("") && !signuppw2.getText().toString().matches("")) {

                        if (signuppw1.getText().toString().equals(signuppw2.getText().toString())) {

                            // TODO SignUP
                            mAuth.createUserWithEmailAndPassword(signupemail.getText().toString(), signuppw1.getText().toString())
                                    .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                ref.orderByChild("email").equalTo(signupemail.getText().toString()).addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                        ref.removeEventListener(this);
                                                        switch(dataSnapshot.child("userrole").getValue().toString()){
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

                                                    }

                                                    @Override
                                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                signupmsg.setText("You don't have any profile yet for " + signupemail.getText() + ". Please contact admin.");

                                            } else {
                                                try {
                                                    throw task.getException();
                                                } catch(FirebaseAuthWeakPasswordException e){
                                                    signupmsg.setText(e.getReason());
                                                }
                                                catch(FirebaseAuthUserCollisionException e){
                                                    signupmsg.setText(e.getMessage());
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }
                                    });

                        } else{
                            signupmsg.setText("Password does not match.");
                        }
                    } else {
                        signupmsg.setText("Please enter a password.");
                    }

                } else {
                    signupmsg.setText("Please enter email.");
                }

            }
        });
    }
}


