package org.octoprint.api.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.PrinterCommand;
import org.octoprint.api.model.PrinterState;
import org.octoprint.api.model.TemperatureInfo;
import org.octoprint.api.test.util.JSONAnswer;
import org.junit.Test;

public class PrinterCommandTest {
	private PrinterCommand command = null;
	
	public PrinterCommandTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("printer_state.json"));
		
		command = new PrinterCommand(i);
	}
	
	@Test
	public void stateTest(){
		PrinterState c = command.getCurrentState();
		
		assertNotNull("Printer State",c);
	}
	
	@Test
	public void checkFlagsTest(){
		PrinterState c = command.getCurrentState();
		
		assertTrue("Is Connected",c.isConnected());
		assertTrue("Is Ready",c.isReady());
		assertTrue("Is Operational",c.isOperational());
		assertTrue("No Error",!c.hasError());
		assertTrue("Not Printing",!c.isPrinting());
		assertTrue("Not Paused",!c.isPaused());
		
	}
	
	@Test
	public void extruderTempTest(){
		//get temp on tool 1
		TemperatureInfo t = command.getExtruderTemp(0);
		
		assertNotNull("Tool exists",t);
		
		//check the temps
		assertEquals("Ext 0 Actual",214.8821,t.getActualTemp(),1);
		assertEquals("Ext 0 Target",220.0,t.getTargetTemp(),1);
		assertEquals("Ext 0 Offset",0,t.getOffset().longValue());
	}
	
	@Test
	public void extruderMissingTest(){
		//extruder 2 does not exist
		TemperatureInfo t = command.getExtruderTemp(1);
		
		assertNull("Extruder 2 Null",t);
	}
	
	@Test
	public void bedTempTest(){
		TemperatureInfo t = command.getBedTemp();
		
		assertNotNull("Bed exists",t);
		
		//check the temps
		assertEquals("Bed Actual",50.221,t.getActualTemp(),1);
		assertEquals("Bed Target",-1,t.getTargetTemp(),1); //there is no bed target in the test data
		assertEquals("Bed Offset",5,t.getOffset().longValue());
	}
}
