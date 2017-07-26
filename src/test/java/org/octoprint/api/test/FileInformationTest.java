package org.octoprint.api.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.FileCommand;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.model.FileType;
import org.octoprint.api.model.OctoPrintFileInformation;
import org.octoprint.api.test.util.JSONAnswer;
import org.octoprint.api.test.util.NullAnswer;

/**
 *  Tests high-level file information as well as the ability to setup files based on type
 * 
 * @author rweber
 */
public class FileInformationTest {
	private FileCommand command = null;
	
	public FileInformationTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("single_file.json"));
		
		command = new FileCommand(i);
	}
	
	@Test
	public void isFileSuccessTest(){
		OctoPrintFileInformation aFile = command.getFileInfo("whistle_v2.gcode");
		
		assertEquals("Is File",aFile.getType(),FileType.MACHINECODE);
	}
	
	@Test
	public void isFolderSuccessTest(){
		//need a folder for this one
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("single_folder.json"));
		
		command = new FileCommand(i);
		
		OctoPrintFileInformation aFile = command.getFileInfo("folderA");
		
		assertEquals("Is Folder",aFile.getType(),FileType.FOLDER);
	}
	
	@Test
	public void isFileMissingTest(){
		//we don't have the file in this instance
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new NullAnswer());
		
		command = new FileCommand(i);
		
		OctoPrintFileInformation aFile = command.getFileInfo("missing.gcode");
		
		assertNull("Filename Missing",aFile);
	}
	
	@Test
	public void fileInformationTest(){
		OctoPrintFileInformation aFile = command.getFileInfo("whistle_v2.gcode");
		
		assertEquals("Filename ","whistle_v2.gcode",aFile.getName());
		assertEquals("Path","whistle_v2.gcode",aFile.getPath());
	}
}
