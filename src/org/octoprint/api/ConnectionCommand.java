package org.octoprint.api;

import org.json.simple.JSONObject;

/**
 * @author rweber
 *
 * Implementation of commands found under the Connection (http://docs.octoprint.org/en/master/api/connection.html) endpoint. 
 *
 */
public class ConnectionCommand extends OctoPrintCommand {

	public ConnectionCommand(OctoPrintInstance requestor) {
		super(requestor, "connection");
	}
	
	private boolean connectionCommand(String type){
		OctoPrintHttpRequest request = this.createRequest();
		
		//set request type and command to send
		request.setType("POST");
		request.addParam("command", type);
	
		return g_comm.executeUpdate(request);
	}
	
	public ConnectionState getCurrentState(){
		ConnectionState result = null;
		
		JSONObject json = g_comm.executeQuery(this.createRequest());
	
		if(json != null && json.containsKey("current"))
		{
			result = new ConnectionState();
			result.loadJSON((JSONObject)json.get("current"));
		}
		
		return result;
	}
	
	public boolean connect(){
		return this.connectionCommand("connect");
	}
	
	public boolean disconnect(){
		return this.connectionCommand("disconnect");
	}

}
