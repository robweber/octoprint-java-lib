package org.octoprint.api.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.JobCommand;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.OctoPrintJob;
import org.octoprint.api.OctoPrintJob.JobProgress;
import org.octoprint.api.test.util.JSONAnswer;

public class JobCommandTest {
	private JobCommand command = null;
	
	public JobCommandTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void beforeTest(){
		//create a fake instance for http simulation
		OctoPrintInstance i = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("job.json"));
		
		command = new JobCommand(i);
	}
	
	@Test
	public void getNameTest(){
		
		assertEquals("Job Name Correct",command.getJobDetails().getName(),"whistle_v2.gcode");
	}
	
	@Test
	public void getEstimatedTimeTest(){
		
		assertEquals("Estimated Time Remaining",8811,(long)command.getJobDetails().getEstimatedPrintTime().longValue());
	}
	
	public void getPercentCompleteTest(){
		JobProgress p = command.getJobDetails().getJobProgress();
		assertEquals("Percent Complete",.23,p.percentComplete(),1);
	}
	
	public void getElaspedTimeTest(){
		JobProgress p = command.getJobDetails().getJobProgress();
		assertEquals("Elapsed Time",(long)276,p.elapsedTime().longValue());
	}
	
	public void getTimeRemainingTest(){
		JobProgress p = command.getJobDetails().getJobProgress();
		assertEquals("Time Remaining",(long)912,p.timeRemaining().longValue());
	}
}
