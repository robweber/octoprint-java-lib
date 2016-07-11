package org.octoprint.api;

import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONUtils;

public class FileCommand extends OctoPrintCommand {

	public FileCommand(OctoPrintInstance requestor) {
		super(requestor, "files");
		
	}

	public OctoPrintFile getFileInfo(String filename){
		OctoPrintFile result = null;	//returns null if file does not exist
		
		//try and find the file
		JSONObject json = this.g_comm.executeQuery(this.createRequest("local/" + filename));
		
		if(json != null)
		{
			result = JSONUtils.createObject(json, OctoPrintFile.class.getName());
		}
		
		return result;
	}
	
	public boolean printFile(String filename){
		OctoPrintHttpRequest request = this.createRequest("local/" + filename);
		request.setType("POST");
		
		//set the payloud
		request.addParam("command", "select");
		request.addParam("print", true);
		
		return g_comm.executeUpdate(request);
	}
	
}
