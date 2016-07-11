package org.octoprint.api;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;

/**
 * @author rweber
 *
 * Representation of the connection state of the printer 
 */
public class ConnectionState implements JSONAware, JSONLoader {
	private JSONObject m_json = null;
	
	public ConnectionState() {
		m_json = new JSONObject();
	}

	/**
	 * @return if OctoPrint is connected to a 3D printer
	 */
	public boolean isConnected(){
		//return true if state does not equal closed
		return !m_json.get("state").equals("Closed");
	}
	
	/**
	 * @return connected baudrate
	 */
	public int getBaudrate(){
		int result = 0;
		
		if(m_json.get("baudrate") != null)
		{
			result = Integer.parseInt(m_json.get("baudrate").toString());
		}
		
		return result;
	}
	
	/**
	 * @return port as a string (most likely in the /dev/tty0 format)
	 */
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
	
	/**
	 * @return name of connection profile
	 */
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
