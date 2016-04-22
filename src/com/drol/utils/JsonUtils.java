package com.drol.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class JsonUtils {
	
	public static Map<String, Object> Json2Map(String json) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			
			if(json != null && json.trim().length() > 0) {
				JSONTokener jsonParser = new JSONTokener(json);
				result = Json2Map((JSONObject)jsonParser.nextValue());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static Map<String, Object> Json2Map(JSONObject jsonObject) {
		Map<String, Object> result = new HashMap<String, Object>();
		Iterator<String> it = jsonObject.keys();
		
		while(it.hasNext()){
			String key = it.next();
			Object value = jsonObject.get(key);
			if(value instanceof JSONObject)
				result.put(key, Json2Map((JSONObject)value));
				
			else if(value instanceof JSONArray)
				result.put(key, Json2Array((JSONArray)value));
			
			else 
				result.put(key, value);
		}
		
		return result;
	}
	
	private static Object[] Json2Array(JSONArray jsonArray) {
		Object[] result = new Object[jsonArray.size()];
		
		for(int i = 0; i < result.length; i ++){
			Object value = jsonArray.get(i);
			if(value instanceof JSONObject)
				result[i] = Json2Map((JSONObject)value);
			
			else if(value instanceof JSONArray)
				result[i] = Json2Array((JSONArray)value);
			
			else 
				result[i] = value;
		}
		
		return result;
	}
}
