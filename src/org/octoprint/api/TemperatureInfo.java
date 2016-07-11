package org.octoprint.api;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;


public class TemperatureInfo implements JSONAware, JSONLoader{
	private String m_name = null;
	private JSONObject m_data = null;
	
	public TemperatureInfo() {
		m_name = "Printer Device";
	}

	public String getName(){
		return m_name;
	}
	
	public void setName(String n){
		m_name = n;
	}
	
	public double getActualTemp(){
		return (Double)m_data.get("actual");
	}
	
	public double getTargetTemp(){
		double result = -1; 	//-1 if no target is set
		
		if(m_data.get("target") != null)
		{
			result = (Double)m_data.get("target");
		}
		
		return result;
	}
	
	public long getOffset(){
		return (Long)m_data.get("offset");
	}
	
	@Override
	public void loadJSON(JSONObject json) {
		m_data = json;
	}

	@Override
	public String toJSONString() {
		return m_data.toJSONString();
	}

}
