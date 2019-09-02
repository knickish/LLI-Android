package com.pwny.sauruk.preptracker.m_JSON;

import android.content.Context;
import android.os.AsyncTask;

import com.pwny.sauruk.preptracker.ViewLabelActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class DeleteLabel extends AsyncTask {
    private int uId;
    private Context context;

    public DeleteLabel(Context passed, int Unique){
        uId = Unique;
        context = passed;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String urlBase = "http://45.77.111.93/deleteItem/"+Integer.toString (uId)+"/"+MyAppApplication.getSessKey ();
        URL url = null;
        try {
            url = new URL(urlBase);
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        String x = serverInterface.returnGet (url);
        System.out.println("Delete label: "+ x);




        return null;
    }
}
