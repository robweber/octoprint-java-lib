package org.octoprint.api.model;

import org.json.simple.JSONObject;

/**
 * Implementation of the File type as described in the API http://docs.octoprint.org/en/master/api/datamodel.html#files
 * 
 * @author rweber
 */
public class OctoPrintFile extends OctoPrintFileInformation {

	public OctoPrintFile(FileType t, JSONObject json) {
		super(t,json);
	}

	public String getHash(){
		return m_data.get("hash").toString();
	}
	
	/**
	 * @return the size in bytes
	 */
	public Long getSize(){
		return new Long(m_data.get("size").toString());
	}
	
	/**
	 * @return the timestamp of when this file was uploaded return in milliseconds
	 */
	public Long getTimestamp(){
		Long unix = new Long(m_data.get("date").toString());
		
		//convert to milliseconds
		return new Long(unix.longValue() * 1000);
	}
	
	/**
	 * @return the print time (in seconds) based on gcode analysis
	 */
	public Long getPrintTime(){
		Long result = new Long(0);
		
		JSONObject gcode = (JSONObject)m_data.get("gcodeAnalysis");
		
		if(gcode != null)
		{
			result = new Long(gcode.get("estimatedPrintTime").toString());
		}
		
		return result;
	}
}
