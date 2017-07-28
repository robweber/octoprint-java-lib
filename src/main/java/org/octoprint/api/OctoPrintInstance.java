package org.octoprint.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.DeserializationException;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

/**
 * Stores information on the OctoPrint instance for communication. Used by all OctoPrintCommand classes to send/receive data. 
 * 
 * @author rweber
 * 
 */
public class OctoPrintInstance {
	private String m_url = null;
	private String m_key = null;
	
	public OctoPrintInstance(String host, int port, String apiKey) {
		this(host,port,apiKey,"");
	}
	
	public OctoPrintInstance(String host, int port, String apiKey, String path){
		m_url = "http://" + host + ":" + port + path;
		m_key = apiKey;
	}
	
	private HttpURLConnection createConnection(OctoPrintHttpRequest request){
		HttpURLConnection connection = null;
		
		try{
			URL apiUrl = new URL(m_url + request.getURL());
		
			//create the connection
			connection = (HttpURLConnection)apiUrl.openConnection();
		
			//set default connection parameters
			connection.setRequestProperty("X-Api-Key", m_key);
			connection.setRequestProperty("Content-Type","application/json");
			connection.setRequestMethod(request.getType());
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			if(request.hasParams())
			{
				OutputStream os = connection.getOutputStream();
				os.write(request.getParams().getBytes());
				os.flush();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return connection;
	}
	
	private String getOutput(HttpURLConnection connection){
		String result = "";
		
		try{
			//pull any results from the connection
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
			String temp = null;
			while((temp = br.readLine()) != null)
			{
				result = result + temp;
			}
		
			connection.disconnect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	protected JsonObject executeQuery(OctoPrintHttpRequest request){
		JsonObject result = null;
		
		//create the connection and get the result
		HttpURLConnection connection = this.createConnection(request);
		
		String jsonString = this.getOutput(connection);
		
		if(jsonString != null && !jsonString.isEmpty())
		{
			try {
				result = (JsonObject)Jsoner.deserialize(jsonString);
			} catch (DeserializationException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	protected boolean executeUpdate(OctoPrintHttpRequest request){
		boolean result = true; //assume this will work
		
		HttpURLConnection connection = this.createConnection(request);
		
		try{
			if(connection.getResponseCode() != 204)
			{
				result = false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
