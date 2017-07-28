package org.octoprint.api;

import org.json.simple.JsonObject;

/**
 * An object to encapsulate the http request 
 * 
 * @author rweber
 *  
 */
public class OctoPrintHttpRequest {
	private String m_url = null;
	private String m_type = null;
	private JsonObject m_params = null;
	
	public OctoPrintHttpRequest(String url) {
		m_url = "/api/" + url;
		m_type = "GET";
		m_params = new JsonObject();
	}
	
	protected String getURL(){
		return m_url;
	}
	
	protected String getType(){
		return m_type;
	}
	
	protected String getParams(){
		return m_params.toJson();
	}
	
	public void setType(String type){
		m_type = type;
	}
	
	public void addParam(String name, Object value){
		m_params.put(name,value);
	}
	
	public void setPayload(JsonObject json){
		m_params = json;
	}
	
	public boolean hasParams(){
		return !m_params.isEmpty();
	}
}
