package org.octoprint.api.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.ConnectionCommand;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.model.ConnectionState;
import org.octoprint.api.test.util.JSONAnswer;

public class ConnectionCommandTest {
	private ConnectionCommand command = null;
	
	public ConnectionCommandTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("connection_state.json"));
		
		command = new ConnectionCommand(i);
	}
	
	@Test
	public void parseConnectionTest(){
		ConnectionState c = command.getCurrentState();
		
		assertNotNull("Command returned",c);
	}
	
	@Test
	public void connectionTest(){
		
		assertTrue("Connection state",command.getCurrentState().isConnected());
	}
	
	@Test
	public void baudTest(){
		assertEquals("Baud Rate",(long)250000,command.getCurrentState().getBaudrate().longValue());
	}
	
	@Test
	public void portTest(){
		assertEquals("Port","/dev/ttyACM0",command.getCurrentState().getPort());
	}
	
	@Test
	public void profileTest(){
		assertEquals("Profile Name","_default",command.getCurrentState().getPrinterProfile());
	}
}
