package com.pwny.sauruk.preptracker.m_JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.PendingIntent.getActivity;

public class serverInterface extends AsyncTask {
    private Context context;

    public serverInterface(Context context2){
        context = context2;
    }

    public void sendPostSaveGet(final URL address, final JSONObject payload, final String filename, final Context cont) {

        Thread thread = new Thread(new Runnable()

        {
            @Override
            public void run() {
                try {

                    HttpURLConnection conn = (HttpURLConnection) address.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("message", payload);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    /* os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8")); */
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    int responseCode =  conn.getResponseCode();// error code
                    String response = ""; // response
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader (conn.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                    conn.disconnect();
                    File desto;
                    desto = cont.getFilesDir();
                    FileWriter file = new FileWriter (desto +"/" + filename, false);
                    file.write(response);
                    file.flush();
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    public void saveGet(final URL address, final String filename, final Context cont) {

        Thread thread = new Thread(new Runnable()

        {
            @Override
            public void run() {
                try {

                    HttpURLConnection conn = (HttpURLConnection) address.openConnection();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    int responseCode =  conn.getResponseCode();// error code
                    String response = ""; // response
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader (conn.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                    conn.disconnect();
                    File desto;
                    desto = cont.getFilesDir();
                    FileWriter file = new FileWriter (desto +"/" + filename, false);
                    file.write(response);
                    file.flush();
                    file.close();
                    conn.disconnect ();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static String sendPostReturnGet(final URL address, final JSONObject payload) {
        String response ="";
        try {
            HttpURLConnection conn = (HttpURLConnection) address.openConnection ();
            conn.setRequestProperty ("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","text/html");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject ();
            jsonParam.put ("message", payload);

            Log.i ("JSON", jsonParam.toString ());
            DataOutputStream os = new DataOutputStream (conn.getOutputStream ());
            /* os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8")); */
            os.writeBytes (jsonParam.toString ());

            os.flush ();
            os.close ();

            Log.i ("STATUS", String.valueOf (conn.getResponseCode ()));
            Log.i ("MSG", conn.getResponseMessage ());
            Log.i ("type", conn.getContentType ());

            int HttpResult = conn.getResponseCode ();// error code
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"));
                } catch (IOException e) {
                    System.out.println("155 IOException");
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                response = sb.toString();
                System.out.println("Built response: "+response);
            } else {
                System.out.println("Error");
            }
            conn.disconnect ();
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return response;
    }
    public static String returnGet(final URL address) {
        String response = new String();
        try {
            HttpURLConnection conn = (HttpURLConnection) address.openConnection ();

            Log.i ("STATUS", String.valueOf (conn.getResponseCode ()));
            Log.i ("MSG", conn.getResponseMessage ());

            int HttpResult = conn.getResponseCode ();// error code
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                response = sb.toString();
            } else {
                try {
                    System.out.println(conn.getResponseMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            conn.disconnect ();
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return response;
    }

    public static boolean checkKey(Context context){
        try{
            try {
                SharedPreferences loginKey = PreferenceManager.getDefaultSharedPreferences(context);
                String sessKey = loginKey.getString ("sessKey","error");
                System.out.println("Current Session Key: "+sessKey);
                Integer temp = Integer.valueOf (sessKey.trim ());
                if (temp>1&&!(sessKey.equalsIgnoreCase ("0"))){
                    String urlString = "http://45.77.111.93/checkForSess/" + sessKey;
                    System.out.println("urlString: "+ urlString);
                    URL url = new URL (urlString);
                    String returnVal = serverInterface.returnGet (url);
                    System.out.println("Value from server: "+ returnVal);
                    String test = returnVal.trim ();
                    Integer checker = Integer.valueOf(test);

                    if(checker==1){
                        return true;
                    }
                }
                else{
                    System.out.println("Returned False");
                    return false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace ();
                System.out.println("Returned False");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace ();
            System.out.println("Returned False");
            return false;
        }
        System.out.println("Returned False");
        return false;
    }


    public void updateDatabases() {
        try{
            UpdateDB up = new UpdateDB (context);
            up.execute ();
        }
        catch(Exception e){
            e.printStackTrace ();
        }
    }



    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
