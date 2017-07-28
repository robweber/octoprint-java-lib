package org.octoprint.api.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

/**
 * @author rweber
 *
 * This is a helper class to create Java objects based on JSON returned results. Classes are loaded via reflection but must implement the JSONLoader interface. 
 *
 */
public class JSONUtils {

	@SuppressWarnings("unchecked")
	public static <V> Map<String,V> loadMap(JsonObject jMap, Class<V> value) throws DeserializationException{
		Map<String,V> result = new HashMap<String,V>();
		
		Iterator<String> iter = jMap.keySet().iterator();
		
		String aKey = null;
		while(iter.hasNext())
		{
			aKey = iter.next();
			
			if(jMap.get(aKey) instanceof JsonObject)
			{
				result.put(aKey.toString(),(V)JSONUtils.createObject(((JsonObject)jMap.get(aKey)).toJson(),value.getName()));
			}
			else
			{
				result.put(aKey.toString(),(V)JSONUtils.createPrimitive(((String)jMap.get(aKey)),value.getName()));
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> loadList(JsonArray jArray, Class<T> type) throws DeserializationException{
		List<T> result = new ArrayList<T>();
		
		for(int count = 0; count < jArray.size(); count ++)
		{
			if(jArray.get(count) instanceof JsonObject)
			{
				result.add((T)JSONUtils.createObject(((JsonObject)jArray.get(count)).toJson(),type.getName()));
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
	
	public static <T> T createObject(String json, String type) throws DeserializationException{
		return JSONUtils.createObject((JsonObject)Jsoner.deserialize(json), type);
	}
	
	public static <T> T createObject(JsonObject json, String type){
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
