package org.octoprint.api;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONLoader;

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
	
	public double getEstimatedPrintTime(){
		return ((Double)m_job.get("estimatedPrintTime"));
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
		
		public double percentComplete(){
			return (Double)m_json.get("completion");
		}
		
		public Long elapsedTime(){
			return (Long)m_json.get("printTime");
		}
		
		public Long timeRemaining(){
			return (Long)m_json.get("printTimeLeft");
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
