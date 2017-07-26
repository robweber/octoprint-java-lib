package org.octoprint.api.test;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.FileCommand;
import org.octoprint.api.FileType;
import org.octoprint.api.OctoPrintFileInformation;
import org.octoprint.api.OctoPrintFolder;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.test.util.JSONAnswer;

public class ListFileTest {
	private FileCommand command = null;
	
	public ListFileTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("list_files.json"));
		
		command = new FileCommand(i);
	}
	
	@Test
	public void listFilesTest(){
		//get a list of files
		List<OctoPrintFileInformation> files = command.listFiles();
		
		//check num at the source level
		assertEquals("Num Files",3,files.size());
		
		//check the types of files
		assertEquals("First File Type",FileType.MACHINECODE,files.get(0).getType());
		assertEquals("Second File Type",FileType.MACHINECODE,files.get(1).getType());
		assertEquals("Third File Type",FileType.FOLDER,files.get(2).getType());
		
	}
	
	@Test
	public void listRecusionTest(){
		List<OctoPrintFileInformation> files = command.listFiles();
		
		OctoPrintFolder folder = (OctoPrintFolder)files.get(2);
		
		assertEquals("Num Files",2,folder.getChildren().size());
	}
}
