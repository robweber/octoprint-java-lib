package org.octoprint.api.test.util;

import org.json.simple.JsonObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Represents a blank response (404, or just blank) from the Octoprint Server
 * 
 * @author rweber
 *
 */
public class NullAnswer implements Answer<JsonObject>{

	public NullAnswer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonObject answer(InvocationOnMock arg0) throws Throwable {
		//octoprint didn't send back anything
		return null;
	}

}
