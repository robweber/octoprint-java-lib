package org.octoprint.api;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.DeserializationException;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;
import org.octoprint.api.exceptions.ConnectionFailedException;
import org.octoprint.api.exceptions.InvalidApiKeyException;
import org.octoprint.api.exceptions.NoContentException;
import org.octoprint.api.exceptions.OctoPrintAPIException;

/**
 * Stores information on the OctoPrint instance for communication. Used by all OctoPrintCommand classes to send/receive data.
 *
 * @author rweber
 *
 */
public class OctoPrintInstance {
	private URL m_url = null;
	private String m_key = null;

	public OctoPrintInstance(URL host,String apiKey){
		m_url = host;
		m_key = apiKey;
	}
	
	public OctoPrintInstance(String host, int port, String apiKey) throws MalformedURLException {
		this(host,port,apiKey,"");
	}

	public OctoPrintInstance(String host, int port, String apiKey, String path) throws MalformedURLException {
		this(new URL("http://" + host + ":" + port + path),apiKey);
	}

	private String getOutput(HttpURLConnection connection)
			throws IOException {
		String result = "";

		try(final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			//pull any results from the connection

			String temp = null;
			while((temp = br.readLine()) != null)
			{
				result = result + temp;
			}

			connection.disconnect();
		}
		catch(final IOException e)
		{
			throw e;
		}
		return result;
	}

	public JsonObject executeQuery(OctoPrintHttpRequest request){
		JsonObject result = null;

		//create the connection and get the result
		final HttpURLConnection connection = request.createConnection(m_url,m_key);

		final String jsonString = handleConnection(connection,200);

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

	public boolean executeUpdate(OctoPrintHttpRequest request){
		final HttpURLConnection connection = request.createConnection(m_url,m_key);
		
		try {
			handleConnection(connection,204);
		} catch(final NoContentException e) {
			return false;
		}
		return true;
	}

	private String handleConnection(final HttpURLConnection connection, final int successStatus) {
		final int statusCode;
		String output;
		
		try {
			statusCode = connection.getResponseCode();
		}
		catch (final IOException e) {
			throw new ConnectionFailedException(e);
		}
		
		try {
			output = this.getOutput(connection);
		}
		catch (final IOException e) {
			output = null;
		}
		
		if(statusCode==successStatus) {
			return output;
		}
		
		if(statusCode== NoContentException.STATUS_CODE) {
			throw new NoContentException(output);
		}
		
		if(statusCode==InvalidApiKeyException.STATUS_CODE) {
			throw new InvalidApiKeyException(output);
		}
		
		//catch if no exception matches but no success
		throw new OctoPrintAPIException(statusCode, output);
	}

}