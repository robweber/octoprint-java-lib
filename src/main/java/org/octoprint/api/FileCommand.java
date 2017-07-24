package org.octoprint.api;

import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONUtils;

/**
 *  Implementation of commands found under the File Operations (http://docs.octoprint.org/en/master/api/files.html) endpoint. 
 * 
 * @author rweber
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
	public OctoPrintFileInformation getFileInfo(String filename){
		OctoPrintFileInformation result = null;	//returns null if file does not exist
		
		//try and find the file
		JSONObject json = this.g_comm.executeQuery(this.createRequest("local/" + filename + "?recursive=true"));
		
		if(json != null)
		{
			//figure out what kind of file this is
			FileType t = FileType.findType(json.get("type").toString());
		
			if(t == FileType.FOLDER)
			{
				result = new OctoPrintFolder(t,json);
			}
			else
			{
				result = new OctoPrintFile(t,json);
			}
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
