package org.octoprint.api.model;

import java.io.IOException;
import java.io.Writer;

import org.json.simple.JsonObject;
import org.json.simple.Jsonable;
import org.octoprint.api.util.JSONLoader;

/**
 * A representation of a File Information on the OctoPrint server http://docs.octoprint.org/en/master/api/datamodel.html#file-information 
 * 
 * This is a top-level type that serves as the extension point for specific Files or Folders
 * 
 * @author rweber
 * 
 */
public abstract class OctoPrintFileInformation implements Jsonable, JSONLoader {
	protected JsonObject m_data = null;
	protected FileType m_type = null;
	
	public OctoPrintFileInformation(FileType t,JsonObject json) {
		m_type = t;
		m_data = json;
	}

	public String getName(){
		return m_data.get("name").toString();
	}
	
	/**
	 * @return the type of file (gcode,model,folder)
	 */
	public FileType getType(){
		return m_type;
	}
	
	public String getPath(){
		return m_data.get("path").toString();
	}
	
	@Override
	public void loadJSON(JsonObject json) {
		m_data = json;
	}

	@Override
	public String toJson() {
		return m_data.toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		writable.write(this.toJson());
	}
}
