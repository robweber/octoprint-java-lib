package org.octoprint.api.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.mockito.Mockito;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.SettingsCommand;
import org.octoprint.api.model.TemperatureProfile;
import org.octoprint.api.test.util.JSONAnswer;
import org.junit.Test;

public class SettingsCommandTest {
	private SettingsCommand command = null;
	
	public SettingsCommandTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("settings.json"));
		
		command = new SettingsCommand(i);
	}
	
	@Test
	public void settingsTest(){
		assertNotNull("Retrieve Settings",command.getAllSettings());
	}
	
	@Test 
	public void printerProfileTest(){
		Map<String,TemperatureProfile> profiles = command.getTemperatureProfiles();
		
		assertTrue("Read profiles",!profiles.isEmpty());
		
		//check one
		TemperatureProfile pla = profiles.get("PLA");
		assertEquals("PLA Bed Temp",60,pla.getBedTemp().longValue());
		assertEquals("PLA Extruder",204,pla.getExtruderTemp().longValue());
	}
}
