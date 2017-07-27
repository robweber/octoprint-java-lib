package org.octoprint.api.test;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.octoprint.api.JobCommand;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.model.OctoPrintJob.JobProgress;
import org.octoprint.api.test.util.JSONAnswer;

/**
 *
 * Test aspects of the JobCommand class
 *
 * @author rweber
 *
 */
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
	public void nameTest(){

		assertEquals("Job Name Correct",command.getJobDetails().getName(),"whistle_v2.gcode");
	}

	@Test
	public void estimatedTimeTest(){

		assertEquals("Estimated Time Remaining",(long)8811,command.getJobDetails().getEstimatedPrintTime().longValue());
	}

	@Test
	public void percentCompleteTest(){

		//check a job at 23%
		JobProgress p = command.getJobDetails().getJobProgress();
		assertEquals("Partial Complete",.23,p.percentComplete(),0.01);

		//check a job at 100%
		OctoPrintInstance i2 = Mockito.mock(OctoPrintInstance.class,new JSONAnswer("100_job.json"));
		command = new JobCommand(i2);

		p = command.getJobDetails().getJobProgress();
		assertEquals("100 Percent Complete",1,p.percentComplete(),0.01);
	}

	@Test
	public void elaspedTimeTest(){
		JobProgress p = command.getJobDetails().getJobProgress();
		assertEquals("Elapsed Time",(long)276,p.elapsedTime().longValue());
	}

	@Test
	public void timeRemainingTest(){
		JobProgress p = command.getJobDetails().getJobProgress();
		assertEquals("Time Remaining",(long)912,p.timeRemaining().longValue());
	}
}
