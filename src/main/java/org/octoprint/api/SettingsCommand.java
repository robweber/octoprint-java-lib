package org.octoprint.api;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.octoprint.api.model.TemperatureProfile;
import org.octoprint.api.util.JSONUtils;

/**
 * Implemention of the settings endpoint, check here for all fields http://docs.octoprint.org/en/master/api/settings.html
 * 
 * @author rweber
 * 
 */
public class SettingsCommand extends OctoPrintCommand {

	public SettingsCommand(OctoPrintInstance requestor) {
		super(requestor, "settings");
	}

	/**
	 * 
	 * @return all the settings as a full JSON Object, could be null if no connectivity
	 */
	public JsonObject getAllSettings(){
		JsonObject result = this.g_comm.executeQuery(this.createRequest());
		
		return result;
	}
	
	/**
	 * Find all the currently setup temperature profiles 
	 * 
	 * @return map of temperature profiles, keys are the profile names
	 */
	public Map<String,TemperatureProfile> getTemperatureProfiles(){
		Map<String,TemperatureProfile> result = new HashMap<String,TemperatureProfile>();
		
		JsonObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null && json.containsKey("temperature"));
		{
			//get the whole tree
			JsonArray profiles = (JsonArray)((JsonObject)json.get("temperature")).get("profiles");
			
			if(profiles != null && profiles.size() > 0)
			{
				TemperatureProfile tProfile = null;
				for(int count = 0; count < profiles.size(); count ++)
				{
					tProfile = JSONUtils.createObject((JsonObject)profiles.get(count), TemperatureProfile.class.getName());

					result.put(tProfile.getName(),tProfile);
				}
			}
		}
		
		return result;
	}
}
