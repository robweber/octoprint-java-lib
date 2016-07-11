package org.octoprint.api;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author rweber
 * 
 *  Implemention of the settings endpoint - this doesn't appear to be documented well in the general documentation. Check here: https://github.com/foosel/OctoPrint/blob/master/src/octoprint/server/api/settings.py
 */
public class SettingsCommand extends OctoPrintCommand {

	public SettingsCommand(OctoPrintInstance requestor) {
		super(requestor, "settings");
	}

	/**
	 * Find all the currently setup temperature profiles 
	 * 
	 * @return list of temperature profiles
	 */
	public List<TemperatureProfile> getTemperatureProfiles(){
		List<TemperatureProfile> result = new ArrayList<TemperatureProfile>();
		
		JSONObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			JSONArray profiles = (JSONArray)((JSONObject)json.get("temperature")).get("profiles");
			
			if(profiles != null && profiles.size() > 0)
			{
				TemperatureProfile tProfile = null;
				for(int count = 0; count < profiles.size(); count ++)
				{
					tProfile = new TemperatureProfile();
					
					tProfile.loadJSON((JSONObject)profiles.get(count));
					
					result.add(tProfile);
				}
			}
		}
		
		return result;
	}
}
