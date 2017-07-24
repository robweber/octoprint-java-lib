package org.octoprint.api.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.FileCommand;
import org.octoprint.api.OctoPrintFile;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.test.util.JSONAnswer;
import org.octoprint.api.test.util.NullAnswer;

public class FileCommandTest {
	private FileCommand command = null;
	
	public FileCommandTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("single_file.json"));
		
		command = new FileCommand(i);
	}
	
	@Test
	public void getFileInfoSuccessTest(){
		OctoPrintFile aFile = command.getFileInfo("whistle_v2.gcode");
		
		assertEquals("Filename","whistle_v2.gcode",aFile.getName());
	}
	
	@Test
	public void getFileInfoFailureTest(){
		//we don't have the file in this instance
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new NullAnswer());
		
		command = new FileCommand(i);
		
		OctoPrintFile aFile = command.getFileInfo("missing.gcode");
		
		assertNull("Filename Missing",aFile);
	}
	
	@Test
	public void getEstimatePrintTest(){
		OctoPrintFile aFile = command.getFileInfo("whistle_v2.gcode");
		
		assertEquals("Estimated Print",1188,aFile.getPrintTime().longValue());
	}
}
