package org.octoprint.api.model;

import java.io.IOException;
import java.io.Writer;

import org.json.simple.JsonObject;
import org.json.simple.Jsonable;
import org.octoprint.api.util.JSONLoader;

/**
 * Represents the current state of the printer http://docs.octoprint.org/en/master/api/datamodel.html#sec-api-datamodel-printer-state
 * 
 * @author rweber
 * 
 */
public final class PrinterState implements Jsonable, JSONLoader {
	private JsonObject m_json = null;
	private String m_text = null;
	
	public PrinterState() {
		m_json = new JsonObject();
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
	public void loadJSON(JsonObject json) {
		m_json = (JsonObject)json.get("flags");
		m_text = json.get("text").toString();
	}

	@Override
	public String toJson() {
		JsonObject result = new JsonObject();
		
		result.put("flags",m_json);
		result.put("text",m_text);
		
		return result.toJson();
	}

	@Override
	public void toJson(Writer arg0) throws IOException {
		arg0.write(this.toJson());
	}

}
