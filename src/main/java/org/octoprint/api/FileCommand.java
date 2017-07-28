package org.octoprint.api;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.octoprint.api.model.FileType;
import org.octoprint.api.model.OctoPrintFile;
import org.octoprint.api.model.OctoPrintFileInformation;
import org.octoprint.api.model.OctoPrintFolder;

/**
 *  Implementation of commands found under the File Operations (http://docs.octoprint.org/en/master/api/files.html) endpoint. 
 * 
 * @author rweber
 */
public class FileCommand extends OctoPrintCommand {

	public FileCommand(OctoPrintInstance requestor) {
		super(requestor, "files");
		
	}

	private OctoPrintFileInformation createFile(JsonObject json){
		OctoPrintFileInformation result = null;
		
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
		
		return result;
	}
	
	/**
	 * Returns a list of all files (folders and files) from the root. checks local storage only
	 * 
	 * @return a list of all files in the root folder, null if an error occurs
	 */
	public List<OctoPrintFileInformation> listFiles(){
		List<OctoPrintFileInformation> result = null;
		
		//get a list of all the files
		JsonObject json = this.g_comm.executeQuery(this.createRequest("local?recursive=true"));
		
		if(json != null)
		{
			result = new ArrayList<OctoPrintFileInformation>();
			
			JsonArray children = (JsonArray)json.getCollection("files");
			
			for(int count = 0; count < children.size(); count ++)
			{
				//for each file create the object and add to the array
				result.add(this.createFile((JsonObject)children.get(count)));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns an object with information on the given filename
	 * 
	 * @param filename the name of the file, assumes it is local and not on the SD card
	 * @return info about the file, will return null if that file does not exist
	 */
	public OctoPrintFileInformation getFileInfo(final String filename){
		OctoPrintFileInformation result = null;	//returns null if file does not exist
		
		//try and find the file
		JsonObject json = this.g_comm.executeQuery(this.createRequest("local/" + filename + "?recursive=true"));
		
		if(json != null)
		{
			result = this.createFile(json);
		}
		
		return result;
	}
	
	/**
	 * This will load and start printing of the given file
	 * 
	 * @param filename the name of the file, assumes it is local and not on the SD card
	 * @return if operation succeeded
	 */
	public boolean printFile(final String filename){
		OctoPrintHttpRequest request = this.createRequest("local/" + filename);
		request.setType("POST");
		
		//set the payloud
		request.addParam("command", "select");
		request.addParam("print", true);
		
		return g_comm.executeUpdate(request);
	}
	
}
