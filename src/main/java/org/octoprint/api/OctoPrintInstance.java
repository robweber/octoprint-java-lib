package org.octoprint.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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

	protected JsonObject executeQuery(OctoPrintHttpRequest request){
		JsonObject result = null;

		//create the connection and get the result
		final HttpURLConnection connection = this.createConnection(request);

		final String jsonString = handleConnection(connection,204);

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
		final HttpURLConnection connection = this.createConnection(request);
		try {
			handleConnection(connection,200);
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