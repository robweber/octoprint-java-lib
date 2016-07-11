package org.octoprint.api;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;

public class ConnectionState implements JSONAware, JSONLoader {
	private JSONObject m_json = null;
	
	public ConnectionState() {
		m_json = new JSONObject();
	}

	public boolean isConnected(){
		//return true if state does not equal closed
		return !m_json.get("state").equals("Closed");
	}
	
	public int getBaudrate(){
		int result = 0;
		
		if(m_json.get("baudrate") != null)
		{
			result = Integer.parseInt(m_json.get("baudrate").toString());
		}
		
		return result;
	}
	
	public String getPort(){
		if(m_json.get("port") != null)
		{
			return m_json.get("port").toString();
		}
		else
		{
			return null;
		}
	}
	
	public String getPrinterProfile(){
		return m_json.get("printerProfile").toString();
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
