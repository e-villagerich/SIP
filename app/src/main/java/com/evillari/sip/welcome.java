package com.evillari.sip;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;

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

    ProfileTracker profileTracker;

    private static final String TAG = "SIP.welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        nu = new NetworkUtils.checkConnectivity(this);

        Intent intent = getIntent();

        if(Profile.getCurrentProfile()==null){
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                    routeToRoleActivity(currentProfile);
                    profileTracker.stopTracking();

                }
            };
        } else {
            routeToRoleActivity(Profile.getCurrentProfile());
        }



    }



    public void routeToRoleActivity(Profile profile) {


        if (AccessToken.getCurrentAccessToken()!=null){


            Calendar c = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(c.getTime());

            Log.d("date created", date);


            ARGS[0] = REQUEST.GETUSERINFO;
            ARGS[1] = "";
            ARGS[2] = profile.getId();
            ARGS[3] = profile.getId();
            ARGS[4] = date;

            Log.d("array created", "");
            URL url = NetworkUtils.buildUrl(ARGS);
            Log.d("url created", url.toString());

            if (nu.isConnected()) {

                welcome.connectSIP cs = new welcome.connectSIP(this);
                cs.execute(url);

            }
        } else {


        }

    }

    public class connectSIP extends AsyncTask<URL, Void, String> {

        Context context;

        public connectSIP(Context context){
            this.context = context.getApplicationContext();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // lm.fbstatus.setText("");
        }

        @Override
        protected String doInBackground(URL... url) {

            return NetworkUtils.getResponseFromHttpUrl(context, url[0]);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {


            Log.d("Received: ", s);
            if (s != null) {
                JsonUtils js = new JsonUtils();
                String result[]= js.processedData(s);
                if (result[0].equals("NO")) {
                    if(AccessToken.getCurrentAccessToken()!=null) {
                        Profile profile = Profile.getCurrentProfile();
                        String userID = profile.getId();
                  //      lm.fbstatus.setText("Contact Admin. Please have your ID registered: " + profile.getId());
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(getApplicationContext(), loginmain.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();


                    }

                }
                if (result[0].equals("YES")) {

                    if (result[5].equals("3")){

                        CacheUtil cu = new CacheUtil();
                        cu.saveSessionInfo(context, result[1], result[4],result[2], result[3], result[6]);
                        cu.saveSessionItem(context, result[13], result[9], result[10]);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("result", result);
                        startActivity(intent);
                        finish();
                    }

                    if (result[5].equals("1")){

                        Intent intent = new Intent(context, adminActivity.class);
                        intent.putExtra("result", result);
                        startActivity(intent);
                        finish();

                    }

                }
            } else {

            }

        }
    }



}
