package com.pwny.sauruk.preptracker.m_JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class addItemTypeServer extends AsyncTask<Object, Void, Void> {
    private ItemType toAdd;
    private Context context;
    public addItemTypeServer(ItemType toAdd, Context passedContext){
        this.toAdd = toAdd;
        context = passedContext;
    }

    @Override
    protected Void doInBackground(Object[] params) {
        JSONObject itemStack = new JSONObject ();
        URL url = null;
        SharedPreferences loginKey = PreferenceManager.getDefaultSharedPreferences(context);
        String sessKey = loginKey.getString ("sessKey","error");
        try {
            itemStack.put ("itemName", toAdd.name);
            itemStack.put ("category", toAdd.category);
            itemStack.put ("lifetime", toAdd.lifetime);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        try {
            String URLString = "http://45.77.111.93/addtype/" + sessKey ;
            url = new URL (URLString);
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        String returnVal = serverInterface.sendPostReturnGet (url, itemStack);
        System.out.println("sessKey from Update"+returnVal);

        return null;
    }
}
