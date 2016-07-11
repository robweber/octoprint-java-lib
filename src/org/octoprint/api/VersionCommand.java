package org.octoprint.api;

import org.json.simple.JSONObject;

/**
 * @author rweber
 * Implementation of the Version (http://docs.octoprint.org/en/master/api/version.html) endpoint 
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
		
		JSONObject json = g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = json.get("api").toString();
		}
		
		return result;
	}
	
	/**
	 * @return the server version
	 */
	public String getServerVersion(){
		String result = null;
		
		JSONObject json = g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = json.get("server").toString();
		}
		
		return result;
	}
}
