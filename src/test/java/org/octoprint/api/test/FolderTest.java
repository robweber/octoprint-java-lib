package org.octoprint.api.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.FileCommand;
import org.octoprint.api.OctoPrintFile;
import org.octoprint.api.OctoPrintFileInformation;
import org.octoprint.api.OctoPrintFolder;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.test.util.JSONAnswer;

public class FolderTest {
	private FileCommand command = null;
	
	public FolderTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("single_folder.json"));
		
		command = new FileCommand(i);
	}
	
	@Test
	public void FolderInfoTest(){
		OctoPrintFileInformation aFolder = command.getFileInfo("folderA");
		
		//convert this to a folder
		OctoPrintFolder fileObj = (OctoPrintFolder)aFolder;
		
		assertEquals("Size",(long)1334,fileObj.getSize().longValue());
	}

	@Test
	public void ChildrenTest(){
		OctoPrintFileInformation aFolder = command.getFileInfo("folderA");
		
		//convert this to a folder
		OctoPrintFolder fileObj = (OctoPrintFolder)aFolder;
		
		//check that there are children
		assertTrue("Children Exist",!fileObj.getChildren().isEmpty());
		
		//check that we have some files
		assertTrue("Children Exist",!fileObj.getFiles().isEmpty());
		
		//check that we have some folders
		assertTrue("Children Exist",!fileObj.getFolders().isEmpty());
		
		//check if one of the subfolders has children
		OctoPrintFolder subFolder = fileObj.getFolders().get(0);
		
		assertNotNull("Sub Folder Exists",subFolder);
		assertTrue("Sub Folder Children Exist",!subFolder.getChildren().isEmpty());
		
		//finally check the size of one of the sub folder files
		OctoPrintFile subFile = subFolder.getFiles().get(0);
		
		assertNotNull("File Exists",subFile);
		assertEquals("File Size",(long)100,subFile.getSize().longValue());
	}
}
