package org.octoprint.api;

/**
 * @author rweber
 * 
 * Command classes (endpoints) extend this class to provide functionality to the library 
 */
public abstract class OctoPrintCommand {
	protected OctoPrintInstance g_comm = null;
	protected String g_base = null;
	public OctoPrintCommand(OctoPrintInstance requestor, String base) {
		g_comm = requestor;
		g_base = base;
	}

	protected OctoPrintHttpRequest createRequest(){
		return this.createRequest("");
	}
	
	protected OctoPrintHttpRequest createRequest(String loc) {
		
		String urlPath = g_base;
		
		if(loc != null && !loc.isEmpty())
		{
			urlPath = urlPath + "/" + loc;
		}
		
		OctoPrintHttpRequest request = new OctoPrintHttpRequest(urlPath);
		
		return request;
	}
}
