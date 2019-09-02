package com.pwny.sauruk.preptracker.m_JSON;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAppApplication extends Application {

    public Map<Integer, ItemType> itemInfo = new HashMap<> ();
    public List<String> list = new ArrayList<> ();
    public Map<String, List<Label>> labelDB = new HashMap<> ();
    public static String sessionKey = "";
    private Context context;

    public MyAppApplication(){
    }

    public MyAppApplication(Context context){
        this.context = context.getApplicationContext ();
    }

    public void rebuildData(){
        SharedPreferences loginKey = PreferenceManager.getDefaultSharedPreferences(context);
        String sessKey = loginKey.getString ("sessKey","error");
        sessionKey = sessKey;


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonLabelDBString = pref.getString ("labelDB","error DB String not loaded");

        Map<String, List<Label>> labelDBInternal = null;
        List<String> listInternal = null;
        try {
            labelDBInternal = JSONStrtoClassList.convert (jsonLabelDBString) ;
            listInternal = new ArrayList<> (labelDBInternal.keySet ());

        } catch (JSONException e) {
            e.printStackTrace ();
        }

        //ItemType database factory
        SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(context);
        String itemDBInfoStr = pref.getString ("itemDB","error DB String not loaded");
        Map<Integer, ItemType> itemInfoInternal = null;
        try {
            itemInfoInternal = ItemTypeData.toList (itemDBInfoStr);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        itemInfo = itemInfoInternal;
        list = listInternal;
        labelDB = labelDBInternal;

    }
    public Map<Integer, ItemType> getItemInfo(){
        return itemInfo;
    }
    public List<String> getList(){
        return list;
    }
    public Map<String, List<Label>> getLabelDB(){
        return labelDB;
    }
    public static String getSessKey(){return sessionKey;}


}
