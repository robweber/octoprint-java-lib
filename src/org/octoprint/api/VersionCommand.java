package org.octoprint.api;

import org.json.simple.JSONObject;

public class VersionCommand extends OctoPrintCommand{
	
	public VersionCommand(OctoPrintInstance requestor) {
		super(requestor,"version");
		
	}

	public String getAPIVersion(){
		String result = null;
		
		JSONObject json = g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = json.get("api").toString();
		}
		
		return result;
	}
	
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
