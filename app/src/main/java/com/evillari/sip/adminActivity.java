package com.evillari.sip;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

import Utilities.JsonUtils;
import Utilities.NetworkUtils;
import Utilities.REQUEST;
import Utilities.UsersList;

public class adminActivity extends AppCompatActivity {

    TextView tv_login_status;
    String Args[]=new String[10];
    String token;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        String result[] = intent.getStringArrayExtra("result");
        token = result[1];
        username = result[4];

        tv_login_status = (TextView)findViewById(R.id.tv_login_status);
        tv_login_status.setText(tv_login_status.getText() + " " + result[4]);



//        Args[0] = REQUEST.ONLINEUSERS;
//        Args[1] = token;
//        Args[2] = username;
//
//        URL url = NetworkUtils.buildUrl(Args);
//        getOnlineUsers getOnlineUsers = new getOnlineUsers(this);
//        getOnlineUsers.execute(url);
//

    }


//    public class getOnlineUsers extends AsyncTask<URL, Void, List<UsersList>>{
//
//        Context context;
//        public getOnlineUsers(Context context){
//            this.context = context.getApplicationContext();
//        }
//
//        @Override
//        protected List<UsersList> doInBackground(URL... url) {
//
//            String js = NetworkUtils.getResponseFromHttpUrl(url[0]);
//
//
//
//        }
//
//        @Override
//        protected void onPostExecute(List<UsersList> usersLists) {
//            super.onPostExecute(usersLists);
//        }
//    }


}
