package com.pwny.sauruk.preptracker.m_JSON;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class addItemServer extends AsyncTask<Object, Void, Void> {
    private Label toAdd;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public addItemServer(Label label, Context passedContext){
        this.toAdd = label;
        context = passedContext;
    }

    @Override
    protected Void doInBackground(Object[] params) {
        JSONObject itemStack = new JSONObject ();
        URL url = null;
        SharedPreferences loginKey = PreferenceManager.getDefaultSharedPreferences(context);
        String sessKey = loginKey.getString ("sessKey","error");
        System.out.println("Adding Item to Server");
        String key1 = "itemNumber";
        String key2 = "uId";


        try {
            itemStack.put (key1, toAdd.itemNumber);
            itemStack.put (key2, toAdd.uId);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        System.out.println("label to add: "+ itemStack);
        try {
            String URLString = "http://45.77.111.93/additem/" + sessKey ;
            url = new URL (URLString);
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        String returnVal = serverInterface.sendPostReturnGet (url, itemStack);

        return null;
    }
}