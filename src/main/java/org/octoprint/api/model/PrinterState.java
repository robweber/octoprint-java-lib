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
		return m_json.getBoolean("operational");
	}
	
	public boolean isConnected(){
		return !m_json.getBoolean("closedOrError");
	}
	
	public boolean isReady(){
		return m_json.getBoolean("ready");
	}
	
	public boolean isPrinting(){
		return m_json.getBoolean("printing");
	}
	
	public boolean isPaused(){
		return m_json.getBoolean("paused");
	}
	
	public boolean hasError(){
		return m_json.getBoolean("error");
	}
	
	@Override
	public String toString(){
		return m_text;
	}
	
	@Override
	public void loadJSON(JsonObject json) {
		m_json = (JsonObject)json.get("flags");
		m_text = json.getString("text");
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
