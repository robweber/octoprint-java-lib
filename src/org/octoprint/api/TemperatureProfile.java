package org.octoprint.api;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;


public class TemperatureProfile implements JSONAware, JSONLoader {
	private JSONObject m_json = null;
	
	public TemperatureProfile() {
		
	}

	public String getName(){
		return m_json.get("name").toString();
	}
	
	public int getBedTemp(){
		return Integer.parseInt(m_json.get("bed").toString());
	}
	
	public int getExtruderTemp(){
		return Integer.parseInt(m_json.get("extruder").toString());
	}
	
	@Override 
	public String toString(){
		return this.getName();
	}
	
	@Override
	public void loadJSON(JSONObject json) {
		m_json = json;
	}

	@Override
	public String toJSONString() {
		return m_json.toJSONString();
	}

}
