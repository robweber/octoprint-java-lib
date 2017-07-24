package org.octoprint.api.util;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author rweber
 *
 * This is a helper class to create Java objects based on JSON returned results. Classes are loaded via reflection but must implement the JSONLoader interface. 
 *
 */
public class JSONUtils {

	public static <K,V> Map<K,V> loadMap(JSONObject jMap, Class<K> key, Class<V> value){
		Map<K,V> result = new HashMap<K,V>();
		
		Iterator<K> iter = jMap.keySet().iterator();
		
		K aKey = null;
		while(iter.hasNext())
		{
			aKey = iter.next();
			
			if(jMap.get(aKey) instanceof JSONObject)
			{
				result.put((K)JSONUtils.createPrimitive(aKey.toString(), key.getName()),(V)JSONUtils.createObject(((JSONObject)jMap.get(aKey)).toJSONString(),value.getName()));
			}
			else
			{
				result.put((K)JSONUtils.createPrimitive(aKey.toString(), key.getName()),(V)JSONUtils.createPrimitive(((String)jMap.get(aKey)),value.getName()));
			}
		}
		
		return result;
	}
	
	public static <T> List<T> loadList(JSONArray jArray, Class<T> type){
		List<T> result = new ArrayList<T>();
		
		for(int count = 0; count < jArray.size(); count ++)
		{
			if(jArray.get(count) instanceof JSONObject)
			{
				result.add((T)JSONUtils.createObject(((JSONObject)jArray.get(count)).toJSONString(),type.getName()));
			}
			else
			{
				result.add((T)JSONUtils.createPrimitive(((String)jArray.get(count)),type.getName()));
			}
			
		}
		
		return result;
	}

	public static <T> T createPrimitive(String json, String type){
		T result = null;
		
		try{
			
			@SuppressWarnings("unchecked")
			Constructor<T> creator = (Constructor<T>)Class.forName(type).getConstructor(String.class);
			result = creator.newInstance(json);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static <T> T createObject(String json, String type){
		return JSONUtils.createObject((JSONObject)JSONValue.parse(json), type);
	}
	
	public static <T> T createObject(JSONObject json, String type){
		T result = null;
		
		try{
			
			@SuppressWarnings("unchecked")
			Constructor<T> creator = (Constructor<T>)Class.forName(type).getConstructor();
			result = creator.newInstance();
			
			//load the object
			((JSONLoader)result).loadJSON(json);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
