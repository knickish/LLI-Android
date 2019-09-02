package com.pwny.sauruk.preptracker.m_JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class UpdateDB extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private MyAppApplication x = null;

    public UpdateDB (Context context2){
        context = context2;
    }
    public UpdateDB (Context context2, MyAppApplication x){
        this.x = x;
        context = context2;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        try{
                SharedPreferences loginKey = PreferenceManager.getDefaultSharedPreferences(context);
                String sessKey = loginKey.getString ("sessKey","error");
                String labelDB = getLabelDB.getDB (sessKey);
                SharedPreferences databaseStrings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = databaseStrings.edit();
                editor.putString ("labelDB", labelDB);
                System.out.println("labelDB string check: " + labelDB);
                editor.apply();
            }
            catch (Exception e){
                e.printStackTrace ();
            }
            try{
                SharedPreferences loginKey = PreferenceManager.getDefaultSharedPreferences(context);
                String sessKey = loginKey.getString ("sessKey","error");
                String itemDB = ItemTypeData.getDB (sessKey);
                System.out.println("itemDB" + itemDB);
                SharedPreferences databaseStrings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = databaseStrings.edit();
                editor.putString ("itemDB", itemDB);
                editor.apply();
            }
            catch (Exception e) {
                e.printStackTrace ();
            }
            if(x!=null){
            x.rebuildData ();
            System.out.println("Not nnull");
            }
            return 1;
        }
    }
