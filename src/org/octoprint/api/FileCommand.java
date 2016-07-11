package org.octoprint.api;

import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONUtils;

/**
 * @author rweber
 *
 * Implementation of commands found under the File Operations (http://docs.octoprint.org/en/master/api/fileops.html) endpoint. 
 */
public class FileCommand extends OctoPrintCommand {

	public FileCommand(OctoPrintInstance requestor) {
		super(requestor, "files");
		
	}

	/**
	 * Returns an object with information on the given filename
	 * 
	 * @param filename the name of the file, assumes it is local and not on the SD card
	 * @return info about the file, will return null if that file does not exist
	 */
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
	
	/**
	 * This will load and start printing of the given file
	 * 
	 * @param filename the name of the file, assumes it is local and not on the SD card
	 * @return if operation succeeded
	 */
	public boolean printFile(String filename){
		OctoPrintHttpRequest request = this.createRequest("local/" + filename);
		request.setType("POST");
		
		//set the payloud
		request.addParam("command", "select");
		request.addParam("print", true);
		
		return g_comm.executeUpdate(request);
	}
	
}
