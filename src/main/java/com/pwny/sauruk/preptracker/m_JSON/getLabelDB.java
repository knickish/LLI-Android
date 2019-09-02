package com.pwny.sauruk.preptracker.m_JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class getLabelDB {

	public static String getDB(String sessKey) throws IOException {
		
		String dbJson = null;
        URL url = new URL("http://45.77.111.93/getAllByCat/"+ sessKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        StringBuilder sb = new StringBuilder();  
        int HttpResult = con.getResponseCode(); 
        if (HttpResult == 200) {
            BufferedReader br = null;
			try {
				br = new BufferedReader(
				        new InputStreamReader(con.getInputStream(), "utf-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }
            br.close();
            dbJson = new String(sb.toString());

        }
        con.disconnect ();
        System.out.println("DBJSON: "+ dbJson);
        return dbJson;
	}

}
