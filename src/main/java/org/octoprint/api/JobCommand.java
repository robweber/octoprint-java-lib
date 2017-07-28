package org.octoprint.api;

import org.json.simple.JSONObject;
import org.octoprint.api.model.OctoPrintJob;
import org.octoprint.api.util.JSONUtils;

/**
 * Implementation of commands found under the Job (http://docs.octoprint.org/en/master/api/job.html) endpoint. 
 * 
 * @author rweber
 */
public class JobCommand extends OctoPrintCommand {
	
	public JobCommand(OctoPrintInstance requestor) {
		super(requestor,"job");

	}
	
	/**
	 * @param newState the new state of the job (start|restart|pause|cancel)
	 * @return if this operation succeeded, will fail if no file is loaded
	 */
	public boolean updateJob(final JobState newState){
		OctoPrintHttpRequest request = this.createRequest();
		
		//set request type and command to send
		request.setType("POST");
		request.addParam("command", newState.getState());
		
		boolean result = this.g_comm.executeUpdate(request);
		
		return result;
	}
	
	/**
	 * @return the details of the current job, return null if no job is loaded
	 */
	public OctoPrintJob getJobDetails(){
		OctoPrintJob result = null;
		
		JSONObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = JSONUtils.createObject(json, OctoPrintJob.class.getName());
		}
		
		return result;
	}
	
	/**
	 * @author rweber
	 * Simple enum to signal the current state of a job
	 */
	public enum JobState{
		START,
		RESTART,
		PAUSE,
		CANCEL;
		
		public String getState(){
			return this.name().toLowerCase();
		}
		
		@Override
		public String toString(){
			return this.getState();
		}
	}
}
