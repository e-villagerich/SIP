package Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.evillari.sip.R;

/**
 * Created by etv on 9/13/2017.
 */

public class CacheUtil {

    public void saveSessionInfo(Context context,String token, String username,  String fname, String lname, String area ){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sessionmoreinfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        SharedPreferences sharedPrefkey = context.getSharedPreferences(context.getResources().getString(R.string.login_info_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor keyEditor = sharedPrefkey.edit();

        keyEditor.putBoolean(context.getResources().getString(R.string.is_logged_in),true);
        editor.putString(context.getResources().getString(R.string.token),token);
        editor.putString(context.getResources().getString(R.string.username),username);
        editor.putString(context.getResources().getString(R.string.fname),fname);
        editor.putString(context.getResources().getString(R.string.lname), lname);
        editor.putString(context.getResources().getString(R.string.area), area);

        editor.apply();
        keyEditor.apply();

    }


    public void saveAdminSession(Context context) {


        SharedPreferences sharedPrefkey = context.getSharedPreferences(context.getResources().getString(R.string.login_admin_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor keyEditor = sharedPrefkey.edit();

        keyEditor.putBoolean(context.getResources().getString(R.string.admin_is_logged_in),true);
        keyEditor.apply();


    }

    public static void clearSessionInfo(Context context){

        SharedPreferences sessionsharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sessionmoreinfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor sessioneditor = sessionsharedPref.edit();

        SharedPreferences itemssharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sessioniteminfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor itemseditor = itemssharedPref.edit();

        SharedPreferences sharedPrefkey = context.getSharedPreferences(context.getResources().getString(R.string.login_info_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor keyEditor = sharedPrefkey.edit();
        keyEditor.putBoolean(context.getResources().getString(R.string.is_logged_in), false);

        sessioneditor.clear();
        sessioneditor.apply();
        itemseditor.clear();
        itemseditor.apply();
        keyEditor.clear();
        keyEditor.apply();


    }

    public static void adminClearSession(Context context){

        SharedPreferences sharedPrefkey = context.getSharedPreferences(context.getResources().getString(R.string.login_admin_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor keyEditor = sharedPrefkey.edit();
        keyEditor.putBoolean(context.getResources().getString(R.string.admin_is_logged_in), false);

        keyEditor.clear();
        keyEditor.apply();

    }

    public String[] getSessioninfo(Context context){

        String sessioninfo[] = new String[8];
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sessionmoreinfo), Context.MODE_PRIVATE);

        String udefault = context.getResources().getString(R.string.defaultusername);
        String fdefault = context.getResources().getString(R.string.defaultfname);
        String ldefault = context.getResources().getString(R.string.defaultlname);
        String adefault = context.getResources().getString(R.string.defaultarea);
        String tdefault = context.getResources().getString(R.string.defaulttoken);

        sessioninfo [1] = sharedPref.getString(context.getResources().getString(R.string.token),tdefault);
        sessioninfo [4] = sharedPref.getString(context.getResources().getString(R.string.username),udefault);
        sessioninfo [2] = sharedPref.getString(context.getResources().getString(R.string.fname),fdefault);
        sessioninfo [3] = sharedPref.getString(context.getResources().getString(R.string.lname), ldefault);
        sessioninfo [6] = sharedPref.getString(context.getResources().getString(R.string.area), adefault);

        return sessioninfo;

    }


    public boolean sessionStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.login_info_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getResources().getString(R.string.is_logged_in), false);

    }

    public boolean adminSessionStatus(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.login_admin_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getResources().getString(R.string.admin_is_logged_in), false);

    }

    public void saveSessionItem(Context context, String instock, String solditem, String soldprice){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sessioniteminfo), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getResources().getString(R.string.instock), instock);
        editor.putString(context.getResources().getString(R.string.solditem), solditem);
        editor.putString(context.getResources().getString(R.string.soldprice), soldprice);
        editor.apply();

    }

    public  String[] getSessionItem(Context context){

        String sessionitems[] = new String[5];
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sessioniteminfo),Context.MODE_PRIVATE);
        String instockdefault = context.getResources().getString(R.string.instockdefault);
        String solditemdefault = context.getResources().getString(R.string.solditemdefault);
        String soldpricedefault = context.getResources().getString(R.string.soldpricedefault);
        sessionitems[0] = sharedPref.getString(context.getResources().getString(R.string.instock), instockdefault);
        sessionitems[1] = sharedPref.getString(context.getResources().getString(R.string.solditem), solditemdefault);
        sessionitems[2] = sharedPref.getString(context.getResources().getString(R.string.soldprice), soldpricedefault);
        return sessionitems;
    }



}
