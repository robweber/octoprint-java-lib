package org.octoprint.api;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
	 * recursive private method for creating the flat map structure from a JSONObject. JSONObjects within the passed in arg will call this method recursively. 
	 * 
	 * @param map current map
	 * @param currentPath the path current on such as {@code "server.diskspace"}. all keys will be added to this path
	 * @param currentNode the current JSONObject the function will parse
	 */
	private void createFlatMap(final Map<String, String> map, final String currentPath, final JSONObject currentNode) {
		for(final Object key : currentNode.keySet()) {
			final String keyPath = currentPath + key;
			final Object value = currentNode.get(key);
			if(value == null) {
				map.put(keyPath, null);
			} else if (value instanceof JSONObject) {
				createFlatMap(map, keyPath + ".", (JSONObject) value);
			} else {
				map.put(keyPath, value.toString());
			}
		}
	}
	
	/**
	 *
	 * @return all the settings as a full JSON Object, could be null if no connectivity
	 */
	public JSONObject getAllSettingsJSON(){
		JSONObject result = this.g_comm.executeQuery(this.createRequest());

		return result;
	}

	/**
	 * Returns a map containing all settings as a flat map. Use the full identifier as key (e.g. {@code "server.diskspace.critical"} to get value as a string.
	 *
	 * @return settings map
	 */
	public Map<String, String> getFlatSettingsMap(){
		final JSONObject settingsNode = getAllSettingsJSON();
		final Map<String, String> settings = new HashMap<>();
		if(settingsNode != null) {
			createFlatMap(settings, "", settingsNode);
		}
		return settings;
	}

	/**
	 * Find all the currently setup temperature profiles
	 *
	 * @return map of temperature profiles, keys are the profile names
	 */
	public Map<String,TemperatureProfile> getTemperatureProfiles(){
		Map<String,TemperatureProfile> result = new HashMap<String,TemperatureProfile>();

		JSONObject json = this.g_comm.executeQuery(this.createRequest());

		if(json != null && json.containsKey("temperature"));
		{
			//get the whole tree
			JSONArray profiles = (JSONArray)((JSONObject)json.get("temperature")).get("profiles");

			if(profiles != null && profiles.size() > 0)
			{
				TemperatureProfile tProfile = null;
				for(int count = 0; count < profiles.size(); count ++)
				{
					tProfile = JSONUtils.createObject((JSONObject)profiles.get(count), TemperatureProfile.class.getName());

					result.put(tProfile.getName(),tProfile);
				}
			}
		}

		return result;
	}
}
