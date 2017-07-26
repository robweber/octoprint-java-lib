package org.octoprint.api.model;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;
import org.octoprint.api.util.JSONUtils;

/**
 * Representation of an OctoPrint Job object. http://docs.octoprint.org/en/master/api/datamodel.html#sec-api-datamodel-jobs-job
 * 
 * @author rweber
 * 
 */
public class OctoPrintJob implements JSONAware, JSONLoader {
	private JSONObject m_job = null;
	private JobProgress m_progress = null;
	
	public OctoPrintJob() {

	}

	/**
	 * @return the name of the file
	 */
	public String getName(){
		String result = null;
		
		if(m_job.containsKey("file"))
		{
			JSONObject file = (JSONObject)m_job.get("file");
			
			if(file.get("name") != null)
			{
				result = file.get("name").toString();
			}
			
		}
		
		return result;
	}
	
	/**
	 * @return the estimated print time for the file, in seconds
	 */
	public Long getEstimatedPrintTime(){
		return ((Long)m_job.get("estimatedPrintTime"));
	}
	
	/**
	 * @return current job progress
	 */
	public JobProgress getJobProgress(){
		return m_progress;
	}
	
	@Override
	public void loadJSON(JSONObject json) {
		m_job = (JSONObject)json.get("job");
		
		if(json.containsKey("progress"))
		{
			m_progress = new JobProgress();
			m_progress.loadJSON((JSONObject)json.get("progress"));
		}
	}

	@Override
	public String toJSONString() {
		return m_job.toJSONString();
	}
	
	public class JobProgress implements JSONAware, JSONLoader {
		private JSONObject m_json = null;
		
		public JobProgress(){
			
		}
		
		public Double percentComplete(){
			return Double.parseDouble(m_json.get("completion").toString());
		}
		
		public Long elapsedTime(){
			return Long.parseLong(m_json.get("printTime").toString());
		}
		
		public Long timeRemaining(){
			return Long.parseLong(m_json.get("printTimeLeft").toString());
		}
		
		@Override
		public void loadJSON(JSONObject json) {
			m_json = json;	
		}

		@Override
		public String toJSONString() {
			return m_json.toJSONString();
		}
		
	}
}
