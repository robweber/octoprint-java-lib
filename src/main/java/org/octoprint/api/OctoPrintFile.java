package org.octoprint.api;

import org.json.simple.JSONAware;

import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;

/**
 * A representation of a File on the OctoPrint server http://docs.octoprint.org/en/master/api/datamodel.html#sec-api-datamodel-files-file 
 * 
 * @author rweber
 * 
 */
public class OctoPrintFile implements JSONAware, JSONLoader {
	private JSONObject m_data = null;
	
	public OctoPrintFile() {
		
	}

	public String getName(){
		return m_data.get("name").toString();
	}
	
	/**
	 * @return the print time (in seconds) based on gcode analysis
	 */
	public double getPrintTime(){
		double result = 0;
		
		JSONObject gcode = (JSONObject)m_data.get("gcodeAnalysis");
		
		if(gcode != null)
		{
			result = (Double)gcode.get("estimatedPrintTime");
		}
		
		return result;
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
