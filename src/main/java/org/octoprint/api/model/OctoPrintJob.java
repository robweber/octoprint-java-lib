package org.octoprint.api.model;

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
		return new Long(m_job.get("estimatedPrintTime").toString());
	}

	/**
	 * @return current job progress
	 */
	public JobProgress getJobProgress(){
		return m_progress;
	}

	/**
	 * Returns information about the expected filament consumption regarding the current print job. Can be {@code null} if {@code toolIndex} is out of range or if details couldn't be retrieved.
	 *
	 * @param toolIndex Number of the tool to get details about
	 * @return filament consumption details
	 */
	public FilamentDetails getFilamentDetails(final int toolIndex) {
		if(this.m_job == null) {
			return null;
		}
		JSONObject m_filament = (JSONObject) this.m_job.get("filament");
		if(m_filament == null) {
			return null;
		}
		if(toolIndex != 0 || !(m_filament.containsKey("length") && m_filament.containsKey("volume"))) {
			m_filament = (JSONObject) m_filament.get("tool"+toolIndex);
		}
		final FilamentDetails details = new FilamentDetails();
		details.loadJSON(m_filament);
		return details;
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
			return new Double(m_json.get("completion").toString());
		}

		public Long elapsedTime(){
			return new Long(m_json.get("printTime").toString());
		}

		public Long timeRemaining(){
			return new Long(m_json.get("printTimeLeft").toString());
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

	/**
	 * Provides information about filament usage.
	 */
	public static final class FilamentDetails implements JSONAware, JSONLoader {
		private JSONObject m_json = null;

		private FilamentDetails(){

		}

		/**
		 * Returns the length of filament. {@code null} if length is not provided.
		 *
		 * @return length in mm
		 */
		public Long length(){
			if(this.m_json == null || !this.m_json.containsKey("length")) {
				return null;
			}
			return new Long(this.m_json.get("length").toString());
		}

		/**
		 * Returns the volume of filament. {@code null} if volume is not provided.
		 *
		 * @return volume in cm³
		 */
		public Double volume(){
			if(this.m_json == null || !this.m_json.containsKey("volume")) {
				return null;
			}
			return new Double(this.m_json.get("volume").toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void loadJSON(final JSONObject json) {
			m_json = json;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toJSONString() {
			return this.m_json.toJSONString();
		}

	}
}
