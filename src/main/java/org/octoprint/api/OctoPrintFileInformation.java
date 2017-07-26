package org.octoprint.api;

import org.json.simple.JSONAware;

import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;

/**
 * A representation of a File Information on the OctoPrint server http://docs.octoprint.org/en/master/api/datamodel.html#file-information 
 * 
 * This is a top-level type that serves as the extension point for specific Files or Folders
 * 
 * @author rweber
 * 
 */
public abstract class OctoPrintFileInformation implements JSONAware, JSONLoader {
	protected JSONObject m_data = null;
	protected FileType m_type = null;
	
	public OctoPrintFileInformation(FileType t,JSONObject json) {
		m_type = t;
		m_data = json;
	}

	public String getName(){
		return m_data.get("name").toString();
	}
	
	public FileType getType(){
		return m_type;
	}
	
	public String getPath(){
		return m_data.get("path").toString();
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
