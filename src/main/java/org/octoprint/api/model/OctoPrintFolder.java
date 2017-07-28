package org.octoprint.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;

/**
 * Implementation of the folder type as described in the API http://docs.octoprint.org/en/master/api/datamodel.html#folders
 * 
 * @author rweber
 */
public final class OctoPrintFolder extends OctoPrintFileInformation {
	private List<OctoPrintFileInformation> m_children = null;
	
	public OctoPrintFolder(FileType t, JsonObject json) {
		super(t, json);
		
		//build the children now so we aren't doing it with ever 'get' call
		this.buildChildren();
	}

	private void buildChildren(){
		m_children = new ArrayList<OctoPrintFileInformation>();
		
		JsonArray children = (JsonArray)m_data.getCollection("children");
		JsonObject aChild = null;
		FileType t = null;
		
		for(int count = 0; count < children.size(); count ++)
		{
			aChild = (JsonObject)children.get(count);
			
			//get the type of child
			t = FileType.findType(aChild.getString("type"));
			
			if(t == FileType.FOLDER)
			{
				//this child will build it's own children
				m_children.add(new OctoPrintFolder(t,aChild));
			}
			else
			{
				m_children.add(new OctoPrintFile(t,aChild));
			}
		}

	}

	/**
	 * @return a list of all the children, files and folders
	 */
	public List<OctoPrintFileInformation> getChildren(){
		return m_children;
	}
	
	/**
	 * @return a filtered list of children that include only file type objects
	 */
	public List<OctoPrintFile> getFiles(){
		List<OctoPrintFile> result = new ArrayList<OctoPrintFile>();
		
		Iterator<OctoPrintFileInformation> iter = m_children.iterator();
		OctoPrintFileInformation aFile = null;
		
		while(iter.hasNext())
		{
			aFile = iter.next();
			
			if(aFile.getType() != FileType.FOLDER)
			{
				result.add((OctoPrintFile)aFile);
			}
		}
		
		return result;
	}
	
	/**
	 * @return a filtered list of children that include only folder type objects
	 */
	public List<OctoPrintFolder> getFolders(){
		List<OctoPrintFolder> result = new ArrayList<OctoPrintFolder>();
		
		Iterator<OctoPrintFileInformation> iter = m_children.iterator();
		OctoPrintFileInformation aFile = null;
		
		while(iter.hasNext())
		{
			aFile = iter.next();
			
			if(aFile.getType() == FileType.FOLDER)
			{
				result.add((OctoPrintFolder)aFile);
			}
		}
		
		return result;
	}
	
	/**
	 * @return the size, in bytes, of the entire folder
	 */
	public Long getSize(){
		return m_data.getLong("size");
	}
}
