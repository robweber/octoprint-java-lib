package org.octoprint.api.model;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;

/**
 * Represents the current state of the printer http://docs.octoprint.org/en/master/api/datamodel.html#sec-api-datamodel-printer-state
 * 
 * @author rweber
 * 
 */
public final class PrinterState implements JSONAware, JSONLoader {
	private JSONObject m_json = null;
	private String m_text = null;
	
	public PrinterState() {
		m_json = new JSONObject();
	}

	public boolean isOperational(){
		return (Boolean)m_json.get("operational");
	}
	
	public boolean isConnected(){
		return !(Boolean)m_json.get("closedOrError");
	}
	
	public boolean isReady(){
		return (Boolean)m_json.get("ready");
	}
	
	public boolean isPrinting(){
		return (Boolean)m_json.get("printing");
	}
	
	public boolean isPaused(){
		return (Boolean)m_json.get("paused");
	}
	
	public boolean hasError(){
		return (Boolean)m_json.get("error");
	}
	
	@Override
	public String toString(){
		return m_text;
	}
	
	@Override
	public void loadJSON(JSONObject json) {
		m_json = (JSONObject)json.get("flags");
		m_text = json.get("text").toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject result = new JSONObject();
		
		result.put("flags",m_json);
		result.put("text",m_text);
		
		return result.toJSONString();
	}

}
