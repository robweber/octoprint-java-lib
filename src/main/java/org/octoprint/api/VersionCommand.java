package org.octoprint.api;

import org.json.simple.JsonObject;

/**
 * Implementation of the Version (http://docs.octoprint.org/en/master/api/version.html) endpoint 
 * 
 * @author rweber
 */
public class VersionCommand extends OctoPrintCommand{
	
	public VersionCommand(OctoPrintInstance requestor) {
		super(requestor,"version");
		
	}

	/**
	 * @return the api version
	 */
	public String getAPIVersion(){
		String result = null;
		
		JsonObject json = g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = json.getString("api");
		}
		
		return result;
	}
	
	/**
	 * @return the server version
	 */
	public String getServerVersion(){
		String result = null;
		
		JsonObject json = g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = json.getString("server");
		}
		
		return result;
	}
}
