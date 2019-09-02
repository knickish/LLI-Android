package com.pwny.sauruk.preptracker.m_JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONStrtoClassList {
	
	
	public static Map<String, List<Label>> convert(String input) throws JSONException{
		String jsonStr = new String();
		for (int i = 0; i<input.length();i++){
			char c = input.charAt(i);
			//if (c != '\\')
			jsonStr+=c;
		}
		Map<String, List<Label>> dbMap = new HashMap<>();
		JSONArray jsonArray = new JSONArray(jsonStr);
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONObject tempObject = jsonArray.getJSONObject(i);
		    List<Label> temp= new ArrayList<Label>();
		    String k = tempObject.keys().next();
		    JSONArray internal = tempObject.getJSONArray(k);
		    for (int j = 0; j < internal.length(); j++) {
		    	
		    	JSONObject tempInternal = internal.getJSONObject(j);
		    	Label tempLabel = new Label("name", "category", tempInternal.getInt("itemNumber"), tempInternal.getInt("lifetime"), tempInternal.getInt("uid"), tempInternal.getInt("initTime"));
		    	temp.add(tempLabel);
		    }
		    dbMap.put(k, temp);
		  }
	    return dbMap;
		}
	
}
