package Utilities;

import android.net.Uri;

import java.net.URL;

/**
 * Created by etv on 8/25/2017.
 */

public abstract class REQUEST {

    public final static String REQUEST_URL = "https://solarice.evillari.com/";
    public final static String REQUEST_PARAM = "request";

    public final static String REQ = "REQUEST";
    public final static String LOGIN = "LOGIN";
    public final static String GETUSERINFO = "GETUSERINFO";
    public final static String USERNAME = "USERNAME";
    public final static String PASSWORD = "PASSWORD";
    public final static String TOKEN = "TOKEN";
    public final static String DATE = "DATE";
    public final static String LOGOUT = "LOGOUT";
    public final static String ADDITEM = "ADDITEM";
    public final static String QUANTITY = "QUANTITY";
    public final static String ADDSTOCK = "ADDSTOCK";
    public final static String ONLINEUSERS = "ONLINEUSERS";
    public final static String ADDUSER = "ADDUSER";
    public final static String DELUSER = "DELUSER";
    public final static String ADDSTORE = "ADDSTORE";
    public final static String DELSTORE = "DELSTORE";
    public final static String GETINFO = "GETINFO";

    public final static String INSTOCK = "";
    public final static String SOLDITEM = "";
    public final static String SOLDPRICE = "";


    public  abstract Uri LoginRequest(String[] Args);
    public  abstract Uri LogoutRequest(String[] Args);
    public abstract Uri AddItem(String[] Args);
    public abstract Uri AddStock(String[] Args);

}
