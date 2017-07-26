package org.octoprint.api.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.FileCommand;
import org.octoprint.api.OctoPrintFile;
import org.octoprint.api.OctoPrintFileInformation;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.test.util.JSONAnswer;

public class FileTest {
	private FileCommand command = null;
	
	public FileTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("single_file.json"));
		
		command = new FileCommand(i);
	}
	
	@Test
	public void fileInfoTest(){
		OctoPrintFileInformation aFile = command.getFileInfo("whistle_v2.gcode");
		
		//convert this to a file
		OctoPrintFile fileObj = (OctoPrintFile)aFile;
		
		assertEquals("Hash","...",fileObj.getHash()); //no real hash, just a test of the value
		assertEquals("Size",(long)1468987,fileObj.getSize().longValue());
		assertEquals("Timestamp",(long)1378847754000L,fileObj.getTimestamp().longValue());
	}
	
	@Test
	public void gcodeAnalysisTest(){
		OctoPrintFileInformation aFile = command.getFileInfo("whistle_v2.gcode");
		
		//convert this to a file
		OctoPrintFile fileObj = (OctoPrintFile)aFile;
		
		assertEquals("Estimated Print",1188,fileObj.getPrintTime().longValue());
	}
}
