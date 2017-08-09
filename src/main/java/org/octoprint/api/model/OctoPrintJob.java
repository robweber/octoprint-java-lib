package org.octoprint.api.model;

import java.io.IOException;
import java.io.Writer;

import org.json.simple.JsonObject;
import org.json.simple.Jsonable;
import org.octoprint.api.util.JSONLoader;

/**
 * Representation of an OctoPrint Job object. http://docs.octoprint.org/en/master/api/datamodel.html#sec-api-datamodel-jobs-job
 *
 * @author rweber
 *
 */
public final class OctoPrintJob implements Jsonable, JSONLoader {
	private JsonObject m_job = null;
	private JobProgress m_progress = null;

	public OctoPrintJob() {
		m_job = new JsonObject();
	}

	/**
	 * @return {@code true} if a file is currently loaded, or printing, {@code false} if no file is loaded 
	 */
	public boolean isFileLoaded(){
		return this.getName() != null; //if there is a name a file is loaded
	}
	
	/**
	 * @return the name of the file
	 */
	public String getName(){
		String result = null;

		if(m_job.containsKey("file"))
		{
			JsonObject file = (JsonObject)m_job.get("file");

			if(file.get("name") != null)
			{
				result = file.getString("name");
			}

		}

		return result;
	}

	/**
	 * @return the estimated print time for the file, in seconds
	 */
	public Long getEstimatedPrintTime(){
		Long result = null;
		
		if(m_job.get("estimatedPrintTime") != null)
		{
			result =  m_job.getLong("estimatedPrintTime");
		}
		
		return result;
	}

	/**
	 * @return current job progress, will be null if no job is loaded
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
		
		JsonObject m_filament = (JsonObject) this.m_job.get("filament");
		
		if(m_filament == null) 
		{
			return null;
		}
		
		//multiple extruders have different json layout
		if(toolIndex != 0 || !(m_filament.containsKey("length") && m_filament.containsKey("volume"))) 
		{
			m_filament = (JsonObject) m_filament.get("tool"+toolIndex);
		}
		
		final FilamentDetails details = new FilamentDetails();
		details.loadJSON(m_filament);
		
		return details;
	}

	@Override
	public void loadJSON(JsonObject json) {
		
		m_job = (JsonObject)json.get("job");

		//make sure the progress exists and is not null
		if(json.containsKey("progress") && ((JsonObject)json.get("progress")).get("printTime") != null)
		{
			m_progress = new JobProgress();
			m_progress.loadJSON((JsonObject)json.get("progress"));
		}
	}

	@Override
	public String toJson() {
		return m_job.toJson();
	}
	
	@Override
	public void toJson(Writer arg0) throws IOException {
		arg0.write(this.toJson());
	}

	public final class JobProgress implements Jsonable, JSONLoader {
		private JsonObject m_json = null;

		private JobProgress(){
			m_json = new JsonObject();
		}

		/**
		 * @return the percentage the job is complete as a decimal, .05 = 5% 1 = 100%
		 */
		public Double percentComplete(){
			return m_json.getDouble("completion");
		}

		/**
		 * @return elapsed time since the job was started, in seconds
		 */
		public Long elapsedTime(){
			return m_json.getLong("printTime");
		}

		/**
		 * @return estimated time remaining on the print, in seconds
		 */
		public Long timeRemaining(){
			return m_json.getLong("printTimeLeft");
		}

		@Override
		public void loadJSON(JsonObject json) {
			m_json = json;
		}

		@Override
		public String toJson() {
			return m_json.toJson();
		}

		@Override
		public void toJson(Writer arg0) throws IOException {
			arg0.write(this.toJson());
		}

	}

	/**
	 * Provides information about filament usage.
	 */
	public static final class FilamentDetails implements Jsonable, JSONLoader {
		private JsonObject m_json = null;

		private FilamentDetails(){
			m_json = new JsonObject();
		}

		/**
		 * Returns the length of filament. {@code null} if length is not provided.
		 *
		 * @return length in mm
		 */
		public Long length(){
			if(!this.m_json.containsKey("length")) 
			{
				return null;
			}
			return this.m_json.getLong("length");
		}

		/**
		 * Returns the volume of filament. {@code null} if volume is not provided.
		 *
		 * @return volume in cm³
		 */
		public Double volume(){
			if(!this.m_json.containsKey("volume")) 
			{
				return null;
			}
			
			return this.m_json.getDouble("volume");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void loadJSON(final JsonObject json) {
			m_json = json;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toJson() {
			return this.m_json.toJson();
		}

		@Override
		public void toJson(Writer arg0) throws IOException {
			arg0.write(this.toJson());
		}

	}
}
