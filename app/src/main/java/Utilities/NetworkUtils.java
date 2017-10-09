package Utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.renderscript.ScriptGroup;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by etv on 8/25/2017.
 */

public class NetworkUtils extends REQUEST {


    public static URL buildUrl(String Args[]) {

        Uri builtUri = null;
        URL url = null;
        NetworkUtils n = new NetworkUtils();

        switch (Args[0]) {

            case REQUEST.GETUSERINFO:

                builtUri = n.LoginRequest(Args);
                try{
                        url = new URL(builtUri.toString());


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return url;

            case REQUEST.LOGOUT:

                builtUri = n.LogoutRequest(Args);

                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return url;
            case REQUEST.ADDITEM:

                builtUri = n.AddItem(Args);

                try{
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return url;

            case REQUEST.ADDSTOCK:

                builtUri = n.AddStock(Args);
                try{
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return url;

            default:
                return null;

        }
    }


    public static SSLSocketFactory buildSslSocketFactory (Context context){

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = context.getResources().getAssets().open("certs/sip.evillari.com.crt");
            InputStream caInput = new BufferedInputStream(is);

            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(),null);
            return sslContext.getSocketFactory();


        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;

    }



    public static String getResponseFromHttpUrl(Context context, URL url) {


        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("solarice.evillari.com", session);

            }
        };


        HttpsURLConnection httpsURLConnection = null;
        BufferedReader reader = null;
        try {

            httpsURLConnection = (HttpsURLConnection)url.openConnection();

          //  httpsURLConnection.setSSLSocketFactory(buildSslSocketFactory(context));
           // httpsURLConnection.setHostnameVerifier(hostnameVerifier);
            httpsURLConnection.connect();
            InputStream in = httpsURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return null;
    }

    @Override
    public Uri LoginRequest(String[] Args) {


        if (Args[1] == "") {
             return Uri.parse(REQUEST_URL).buildUpon()
                    .appendQueryParameter(REQ, GETUSERINFO)
                    .appendQueryParameter(USERNAME, Args[2])
                    .appendQueryParameter(PASSWORD, Args[3])
                    .appendQueryParameter(DATE, Args[4])
                    .build();



        } else {

            return null;

        }

    }

    @Override
    public Uri LogoutRequest(String[] Args) {
        return Uri.parse(REQUEST_URL).buildUpon()
                .appendQueryParameter(REQ, LOGOUT)
                .appendQueryParameter(TOKEN, Args[1])
                .appendQueryParameter(USERNAME, Args[2])
                .build();

    }

    @Override
    public Uri AddItem(String[] Args) {
        return Uri.parse(REQUEST_URL).buildUpon()
                .appendQueryParameter(REQ, ADDITEM)
                .appendQueryParameter(TOKEN, Args[1])
                .appendQueryParameter(USERNAME, Args[2])
                .appendQueryParameter(QUANTITY, Args[3])
                .appendQueryParameter(DATE, Args[4])
                .build();
    }

    @Override
    public Uri AddStock(String[] Args) {
        return Uri.parse(REQUEST_URL).buildUpon()
                .appendQueryParameter(REQ, ADDSTOCK)
                .appendQueryParameter(TOKEN, Args[1])
                .appendQueryParameter(USERNAME, Args[2])
                .appendQueryParameter(QUANTITY, Args[3])
                .appendQueryParameter(DATE, Args[4])
                .build();

    }


    public static class checkConnectivity {
        Context context;


        public checkConnectivity(Context context) {

            this.context = context.getApplicationContext();

        }

        public String TypeofConnection(){

            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


            if(activeNetwork.getType() == cm.TYPE_WIFI){
                return "WIFI";
            }else if (activeNetwork.getType() == cm.TYPE_MOBILE){
                return "Mobile Connection";
            }else if(activeNetwork.getType() == cm.TYPE_MOBILE_DUN){
                return "Mobile DUN";
            }else if(activeNetwork.getType() == cm.TYPE_WIMAX){
                return "WIMAX";
            }else if(activeNetwork.getType() == cm.TYPE_ETHERNET){
                return "Ethernet";
            }else if(activeNetwork.getType() == cm.TYPE_VPN){
                return "VPN";
            }else {
                return "No Active Connection Available.";
            }

        }

        public boolean isConnected(){

            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            return activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        }

    }

    public static class NetworkChangeReceiver extends BroadcastReceiver{

        Context context;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context.getApplicationContext();

            NetworkUtils.checkConnectivity nc = new NetworkUtils.checkConnectivity(context);
            boolean isConnected = nc.isConnected();

            if(!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())){
                if(!isConnected){

                } else {

                }
            }

        }

    }

}



