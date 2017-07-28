package org.octoprint.api.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * 
 * This is a helper class to emulate reponses from a real OctoPrint server for testing. Responses are read in from JSON files obtained from examples in the OctoPrint REST documentation (http://docs.octoprint.org/en/master/api/index.html).
 * 
 * @author rweber
 *
 */
public class JSONAnswer implements Answer<JsonObject>{
	private String m_json = null;
	
	public JSONAnswer(String filename) {
		m_json = this.readFile(filename);
	}

	private String readFile(String filename){
		String result = null;
		File f = new File(this.getClass().getClassLoader().getResource(filename).getFile());
		
		if(f.exists())
		{
			result = "";
			try{
				BufferedReader reader = new BufferedReader(new FileReader(f));
			
				while(reader.ready())
				{
					result = result + reader.readLine();
				}
			
				reader.close();
			}
			catch(IOException io)
			{
				result = null;
				io.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public JsonObject answer(InvocationOnMock arg0) throws Throwable {
		return (JsonObject)Jsoner.deserialize(m_json);
	}

}
