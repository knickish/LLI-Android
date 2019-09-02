package com.pwny.sauruk.preptracker.m_JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemTypeData {
	
	public static String getDB(String sessKey) throws IOException {
		
		String dbJson = null;
        URL url = new URL("http://45.77.111.93/getAllItemTypes/"+ sessKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        StringBuilder sb = new StringBuilder();  
        int HttpResult = con.getResponseCode(); 
        if (HttpResult == HttpURLConnection.HTTP_OK) {
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
        } else {
            try {
				System.out.println(con.getResponseMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }  
        
        return dbJson;
	}
	 public static Map<Integer, ItemType> toList(String input) throws JSONException{
		 
		 String jsonStr = new String();
			for (int i = 0; i<input.length();i++){
				char c = input.charAt(i);
				//if (c != '\\')
				jsonStr+=c;
			}
		Map<Integer, ItemType> listOfItem = new HashMap<>();
		JSONArray array = new JSONArray(jsonStr);
		for (int i = 0; i < array.length(); i++){
			JSONObject temp = array.getJSONObject(i);
			ItemType item = new ItemType(temp.getString("itemName"), temp.getString("category"),temp.getInt("itemNumber"),temp.getInt("lifetime"));
			listOfItem.put(item.itemNumber, item);
		}
		 
		 return listOfItem;
		 
	 }

}
