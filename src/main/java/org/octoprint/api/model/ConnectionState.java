package org.octoprint.api.model;
import java.io.IOException;
import java.io.Writer;

import org.json.simple.JsonObject;
import org.json.simple.Jsonable;
import org.octoprint.api.util.JSONLoader;

/**
 * Representation of the connection state of the printer 
 * 
 * @author rweber
 */
public final class ConnectionState implements Jsonable, JSONLoader {
	private JsonObject m_json = null;
	
	public ConnectionState() {
		m_json = new JsonObject();
	}

	/**
	 * @return if OctoPrint is connected to a 3D printer
	 */
	public boolean isConnected(){
		//return true if state does not equal closed
		return !m_json.get("state").equals("Closed");
	}
	
	/**
	 * @return connected baudrate, null if nothing available
	 */
	public Long getBaudrate(){
		Long result = null;
		
		if(m_json.get("baudrate") != null)
		{
			result = new Long(m_json.get("baudrate").toString());
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
	public void loadJSON(JsonObject json) {
		m_json = json;
	}

	@Override
	public String toJson() {
		return m_json.toJson();
	}

	@Override
	public void toJson(Writer arg0) throws IOException {
		arg0.write(this.toJson());
	}

}
