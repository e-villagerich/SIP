package com.evillari.sip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import Utilities.AddMoreStock;
import Utilities.CacheUtil;
import Utilities.JsonUtils;
import Utilities.LogoutDialog;
import Utilities.NetworkUtils;
import Utilities.REQUEST;

public class MainActivity extends AppCompatActivity implements AddMoreStock.AddMoreStockListener, LogoutDialog.LogoutDialogListener{

    TextView tv_username;
    TextView tv_area;
    TextView tv_name;
    TextView tv_title_line;
    TextView tv_soldprice;
    TextView tv_instock;
    TextView tv_solditem;

    String ARGS[] = new String[10];
    String token ="";
    String username = "";
    String instock = "", solditem = "", soldprice = "";
    String date="01/01/1980";


    EditText et_quantity;
    Button btn_minus;
    Button btn_plus;
    Button submit_button;

    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        CacheUtil cu = new CacheUtil();
        String sessionitem[] = cu.getSessionItem(this);

        Intent intent = getIntent();
        String result[] = intent.getStringArrayExtra("result");


        tv_username = (TextView)findViewById(R.id.tv_username);
        tv_area = (TextView)findViewById(R.id.tv_area);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_title_line = (TextView)findViewById(R.id.title_line);
        tv_soldprice = (TextView)findViewById(R.id.sales_hit);
        tv_solditem=(TextView)findViewById(R.id.tv_solditem);
        tv_instock = (TextView)findViewById(R.id.total_counter);

        et_quantity = (EditText)findViewById(R.id.input_q);
        btn_minus = (Button)findViewById(R.id.minus_button);
        btn_plus = (Button)findViewById(R.id.plus_button);
        submit_button = (Button)findViewById(R.id.submit_btn);

        profilepic = (ImageView)findViewById(R.id.iv_profile_pic);

        token = result[1];
        username = result[4];

        tv_solditem.setText(sessionitem[1]);
        tv_instock.setText(sessionitem[0]);
        tv_soldprice.setText(sessionitem[2]);

        et_quantity.setFilters(new InputFilter[]{new InputFilterMinMax(1, 12)});
        tv_username.setText(result[4]);
        tv_area.setText(result[6]);

        registerForContextMenu(tv_title_line);


        displayProfile();



    }


    private void displayProfile() {

        Profile profile = Profile.getCurrentProfile();
        tv_name.setText(profile.getName());
        Uri uri = profile.getProfilePictureUri(100,100);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(MainActivity.this)
                .load(uri)
                .transform(transformation)
                .into(profilepic);
    }



    public void plusFunction(View view){

        if (!et_quantity.getText().toString().matches(""))  {

            if((Integer.parseInt(et_quantity.getText().toString()) >= 1) &&  (Integer.parseInt(et_quantity.getText().toString()) < 12)) {
                et_quantity.setText(String.valueOf(Integer.parseInt(et_quantity.getText().toString()) + 1));
            }

        } else {
            et_quantity.setText("1");
        }

    }

    public void minusFunction(View view){

        if (!et_quantity.getText().toString().matches(""))  {

            if((Integer.parseInt(et_quantity.getText().toString()) > 1) &&  (Integer.parseInt(et_quantity.getText().toString()) <= 12)) {
                et_quantity.setText(String.valueOf(Integer.parseInt(et_quantity.getText().toString()) - 1));
            }

        } else {
            et_quantity.setText("1");
        }

    }

    public void addInventory(View view) {

        android.support.v4.app.DialogFragment fm = new AddMoreStock();
        fm.show(getSupportFragmentManager(), "Add Stock");


    }

    @Override
    public void onFinishInput(String input) {

        if (!input.equals("")) {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(c.getTime());

            ARGS[0] = REQUEST.ADDSTOCK;
            ARGS[1] = token;
            ARGS[2] = username;
            ARGS[3] = input;
            ARGS[4] = date;

            Log.d("ADDING STOCK DATE: => ", date);

            Log.d("calling uri @addstock", "");
            URL url = NetworkUtils.buildUrl(ARGS);
            Log.d("URI created", url.toString());
            submitMoreStock sms = new submitMoreStock(this);
            sms.execute(url);

        } else {


            addMoreItemDialog amd = new addMoreItemDialog();
            amd.message = "Stocks: Invalid amount.";
            FragmentManager fm = getSupportFragmentManager();
            amd.show(fm, "Solar-ICE Progam");

        }

    }

    @Override
    public void onLogout(Boolean logout) {


        if (logout){

            Log.d("logout state", String.valueOf(logout));
            ARGS[0] = REQUEST.LOGOUT;
            ARGS[1] = token;
            ARGS[2] = username;

            Log.d("creating url","creating url");
            URL url = NetworkUtils.buildUrl(ARGS);
            Log.d("URL created", url.toString());
            logoutSIP ls = new logoutSIP(this);
            ls.execute(url);

        }

    }

    public class submitMoreStock extends AsyncTask<URL, Void, String>{

        Context context;
        public submitMoreStock(Context context){
            this.context = context.getApplicationContext();
        }


        @Override
        protected String doInBackground(URL... url) {
            return NetworkUtils.getResponseFromHttpUrl(context, url[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){

                JsonUtils ju = new JsonUtils();
                String result[] = ju.parsedAfterSS(s);
                if(result[0].equals("YES")){

                    CacheUtil cu = new CacheUtil();
                    cu.saveSessionItem(context, result[2], result[3], result[4]);

                    tv_username.setText(result[1]);
                    tv_instock.setText(result[2]);
                    instock.equals(result[2]);
                    tv_solditem.setText(result[3]);
                    solditem.equals(result[3]);
                    tv_soldprice.setText(result[4]);
                    soldprice.equals(result[4]);



                }
                if(result[0].equals("NO")){

                    addMoreItemDialog amd = new addMoreItemDialog();
                    FragmentManager fm = getSupportFragmentManager();
                    amd.message = result[1];
                    amd.show(fm,"Solar-ICE Program");

                }

            }else{

            }

        }
    }



    private class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    public void submitFunction(View view){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(c.getTime());


        ARGS[0] = REQUEST.ADDITEM;
        ARGS[1] = token;
        ARGS[2] = username;
        ARGS[3] = et_quantity.getText().toString();
        ARGS[4] = date;

        Log.d("calling urib for ai", "");
        URL url = NetworkUtils.buildUrl(ARGS);
        Log.d("URI created", url.toString());
        submitItem si = new submitItem(this);
        si.execute(url);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if (item.getItemId() == R.id.action_logout){

                Log.d("Logout Yes selected", "clicked Yes");


                LogoutDialog ld = new LogoutDialog();
                FragmentManager fm = getSupportFragmentManager();
                ld.show(fm, "Solar ICE Program");


            }
            return true;
    }

    public class submitItem extends  AsyncTask<URL,Void,String>{

        Context context;

        public submitItem(Context context) {
            this.context=context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {

            submit_button.setEnabled(false);

        }

        @Override
        protected String doInBackground(URL... url) {

            return NetworkUtils.getResponseFromHttpUrl(context, url[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            submit_button.setEnabled(true);

            if(s!=null){

                JsonUtils ju = new JsonUtils();
                String result[] = ju.parsedAfterAI(s);
                if(result[0].equals("YES")){


                    CacheUtil cu = new CacheUtil();
                    cu.saveSessionItem(context, result[2], result[3], result[4]);

                    tv_username.setText(result[1]);
                    tv_instock.setText(result[2]);
                    instock.equals(result[2]);
                    tv_solditem.setText(result[3]);
                    solditem.equals(result[3]);
                    tv_soldprice.setText(result[4]);
                    soldprice.equals(result[4]);

                }
                if(result[0].equals("NO")){

                    addMoreItemDialog amd = new addMoreItemDialog();
                    amd.message = result[1];

                    FragmentManager fm = getSupportFragmentManager();
                    amd.show(fm,"Solar-ICE Program.");


                }

            } else {

            }

        }
    }

    public class logoutSIP extends AsyncTask<URL, Void, String> {

        Context context;
        public logoutSIP(Context context){
            this.context= context.getApplicationContext();
        }


        @Override
        protected String doInBackground(URL... url) {

            return NetworkUtils.getResponseFromHttpUrl(context, url[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            if(s!=null){

                JsonUtils js = new JsonUtils();
                String result[] = js.parsedLogoutString(s);
                if(result[0].equals("YES")){

                    LoginManager.getInstance().logOut();
                    CacheUtil.clearSessionInfo(context);

                    Intent intent = new Intent(context, loginmain.class);
                    intent.putExtra("logout", "logout");
                    startActivity(intent);
                    finish();
                }


            }

        }
    }

    public static  class addMoreItemDialog extends android.support.v4.app.DialogFragment {

        String message = "";

        public addMoreItemDialog(){

        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }



}
