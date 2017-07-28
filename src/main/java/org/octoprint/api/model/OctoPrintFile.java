package org.octoprint.api.model;

import org.json.simple.JsonObject;

/**
 * Implementation of the File type as described in the API http://docs.octoprint.org/en/master/api/datamodel.html#files
 * 
 * @author rweber
 */
public final class OctoPrintFile extends OctoPrintFileInformation {

	public OctoPrintFile(FileType t, JsonObject json) {
		super(t,json);
	}

	public String getHash(){
		return m_data.getString("hash");
	}
	
	/**
	 * @return the size in bytes
	 */
	public Long getSize(){
		return m_data.getLong("size");
	}
	
	/**
	 * @return the timestamp of when this file was uploaded, seconds
	 */
	public Long getTimestamp(){
		return m_data.getLong("date");
	}
	
	/**
	 * @return the print time (in seconds) based on gcode analysis
	 */
	public Long getPrintTime(){
		Long result = new Long(0);
		
		JsonObject gcode = (JsonObject)m_data.get("gcodeAnalysis");
		
		if(gcode != null)
		{
			result = gcode.getLong("estimatedPrintTime");
		}
		
		return result;
	}
}
