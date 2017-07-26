package org.octoprint.api.model;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;


/**
 * Temperature Profile information as provided by OctoPrint Settings - currently doesn't handle more than one extruder!
 * 
 * @author rweber
 */
public class TemperatureProfile implements JSONAware, JSONLoader {
	private JSONObject m_json = null;
	
	public TemperatureProfile() {
		
	}

	public String getName(){
		return m_json.get("name").toString();
	}
	
	public Long getBedTemp(){
		return new Long(m_json.get("bed").toString());
	}
	
	public Long getExtruderTemp(){
		return new Long(m_json.get("extruder").toString());
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
