package Utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by etv on 8/28/2017.
 */

public class JsonUtils {

    public static String[] processedData(String js){

        String request = "";
        String r[] = new String[10];
        r[0] = "no data";

        try {
            JSONObject jsonObject = new JSONObject(js);
            request = jsonObject.getString("REQUEST");

            Log.d("parsed @ processedData", request);

            switch (request){

                case REQUEST.LOGIN:
                    Log.d("pass to login function", request);
                    return parsedLoginString(js);
                case REQUEST.ADDITEM:
                    Log.d("to after adding item", request);
                    return parsedAfterAI(js);
                case REQUEST.ADDSTOCK:
                    Log.d("adding stock", request);
                    return  parsedAfterSS(js);
                case REQUEST.LOGOUT:
                    Log.d("logout ", request);
                    return parsedLogoutString(js);
                default:
                    break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return r;
    }


    public static String[] parsedLogoutString(String js){
        Log.d("logoutstring json data", js);
        String result[] = new String [5];

        try {
            JSONObject jsonObject = new JSONObject(js);
            String success = jsonObject.getString("SUCCESS");
            Log.d("logoutstring success ", success);

            if(success.equals("YES")){

                Log.d("parsing yes", success);
                JSONObject status = jsonObject.getJSONObject("STATUS");
                result[0] = success;
                result[1] = status.getString("LOGOUT");

                Log.d("after logoutparsing yes",result[0]);
                return result;
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String[] parsedAfterSS(String js){
        Log.d("afterss json data", js);
        String result[] = new String[15];

        try {
            JSONObject jsonObject = new JSONObject(js);
            String status = jsonObject.getString("SUCCESS");
            Log.d("get afterss status", status);

            if(status.equals("YES")){

                Log.d("parsing afterss", js);
                JSONObject newinventory = jsonObject.getJSONObject("NEWINVENTORYSTOCK");

                result[0] = status;
                result[1]=newinventory.getString("USER");
                result[2]=newinventory.getString("INSTOCK");
                result[3]=newinventory.getString("SOLDITEM");
                result[4]=newinventory.getString("SOLDPRICE");
                result[5]=newinventory.getString("REGPRICE");
                result[6]=newinventory.getString("SALEPRICE");

                Log.d("return afterss yes", result[0]);
                return result;
            }
            if(status.equals("NO")){

                Log.d("parsing afterss no", js);
                result[0] = status;
                result[1] = jsonObject.getString("REASON");

                Log.d("return after ss no", result[0]);
                return result;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("exit afterss function", js);
        return result;

    }

    public static String[] parsedAfterAI(String js){

        Log.d("afterAI json data", js);
        String result[] = new String[15];

        try {
            JSONObject jsonObject = new JSONObject(js);
            String status = jsonObject.getString("SUCCESS");
            Log.d("get afterai status", status);

            if(status.equals("YES")){

                Log.d("parsing afterai yes", js);
                JSONObject inventory = jsonObject.getJSONObject("NEWINVENTORY");

                result[0] = status;
                result[1] = inventory.getString("USER");
                result[2] = inventory.getString("INSTOCK");
                result[3] = inventory.getString("SOLDITEM");
                result[4] = inventory.getString("SOLDPRICE");

                Log.d("return after ai yes", result[0]);
                return  result;

            }
            if(status.equals("NO")){

                Log.d("parsing afterai no", js);
                result[0] = status;
                result[1] = jsonObject.getString("REASON");

                Log.d("return after ai no", result[0]);
                return result;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("exit afterai function", js);
        return result;

    }


    public static String[] parsedLoginString(String js){

        Log.d("login json data", js);
        String result[] = new String[15];

        try {
            JSONObject jsonObject = new JSONObject(js);
            String status = jsonObject.getString("SUCCESS");
            Log.d("get login status", status);

            if (status.equals("YES")) {

                Log.d("parsing array", js);
                JSONArray userinfo = jsonObject.getJSONArray("USERINFO");

                result[0]=status;
                result[1] = jsonObject.getString("TOKEN");
                result[2]=userinfo.getJSONObject(0).getString("FIRSTNAME");
                result[3]=userinfo.getJSONObject(0).getString("LASTNAME");
                result[4]=userinfo.getJSONObject(0).getString("USERNAME");
                result[5]=userinfo.getJSONObject(0).getString("ROLE");
                result[6]=userinfo.getJSONObject(0).getString("AREA");


                if(result[5].equals("3")) {


                    JSONArray inventory = jsonObject.getJSONArray("INVENTORY");
                    result[7] = inventory.getJSONObject(0).getString("ADDEDQ");
                    result[8] = inventory.getJSONObject(0).getString("BATCHQ");
                    result[9] = inventory.getJSONObject(0).getString("SOLDITEM");
                    result[10] = inventory.getJSONObject(0).getString("SOLDPRICE");
                    result[11] = inventory.getJSONObject(0).getString("BATCHDATE");
                    result[12] = inventory.getJSONObject(0).getString("ICEQDATE");
                    result[13] = inventory.getJSONObject(0).getString("INSTOCK");

                }

                Log.d("parsedLogin true: ", result[0]);

                return result;

            } if (status.equals("NO")) {

                Log.d("parsing array", js);

                result[0] = status;
                result[1] = "Login Failed.";
                result[2] = "Please Try again.";

                Log.d("parsedLogin false: ", result[0]);

                return result;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("exiting login function", js);
        return result;
    }

}
