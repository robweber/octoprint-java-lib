package org.octoprint.api;

import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONUtils;

public class JobCommand extends OctoPrintCommand {
	
	public JobCommand(OctoPrintInstance requestor) {
		super(requestor,"job");

	}
	
	public boolean updateJob(JobState newState){
		OctoPrintHttpRequest request = this.createRequest();
		
		//set request type and command to send
		request.setType("POST");
		request.addParam("command", newState.getState());
		
		boolean result = this.g_comm.executeUpdate(request);
		
		return result;
	}
	
	public OctoPrintJob getJobDetails(){
		OctoPrintJob result = null;
		
		JSONObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = JSONUtils.createObject(json, OctoPrintJob.class.getName());
		}
		
		return result;
	}
	
	public enum JobState{
		START("start"),
		RESTART("restart"),
		PAUSE("pause"),
		CANCEL("cancel");
		
		private String m_state = null;
		
		JobState(String state){
			m_state = state;
		}
		
		public String getState(){
			return m_state;
		}
	}
}
