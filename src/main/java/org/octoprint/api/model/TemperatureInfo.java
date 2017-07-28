package org.octoprint.api.model;

import java.io.IOException;
import java.io.Writer;

import org.json.simple.JsonObject;
import org.json.simple.Jsonable;
import org.octoprint.api.util.JSONLoader;


/**
 * Temperature information as provided by an extruder or the print bed http://docs.octoprint.org/en/master/api/datamodel.html#sec-api-datamodel-printer-tempdata
 * @author rweber
 */
public final class TemperatureInfo implements Jsonable, JSONLoader{
	private String m_name = null;
	private JsonObject m_data = null;
	
	public TemperatureInfo() {
		m_data = new JsonObject();
		m_name = "Printer Device";
	}

	/**
	 * @return the name of tool (Extruder, Print Bed, etc)
	 */
	public String getName(){
		return m_name;
	}
	
	public void setName(String n){
		m_name = n;
	}
	
	/**
	 * @return the actual Temp of the tool in degrees celsius
	 */
	public Double getActualTemp(){
		return m_data.getDouble("actual");
	}
	
	/**
	 * @return the target temp in degrees celsius, returns -1 if no target is set
	 */
	public Double getTargetTemp(){
		return m_data.getDoubleOrDefault("target", -1);
	}
	
	/**
	 * @return the offset, 0 if none
	 */
	public Long getOffset(){
		return m_data.getLong("offset");
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
	public void toJson(Writer arg0) throws IOException {
		arg0.write(this.toJson());
	}

}
