package org.octoprint.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.octoprint.api.util.JSONUtils;

/**
 * Implementation of commands found under the Printer (http://docs.octoprint.org/en/master/api/printer.html) endpoint. 
 * 
 * @author rweber
 * 
 */
public class PrinterCommand extends OctoPrintCommand {
	
	public PrinterCommand(OctoPrintInstance requestor) {
		super(requestor,"printer");
	}

	private boolean sendCommand(String path, JSONObject payload){
		OctoPrintHttpRequest request = this.createRequest(path);
		
		//set request type and command to send
		request.setType("POST");
		
		//create the json params to send
		request.setPayload(payload);
		
		return g_comm.executeUpdate(request);
	}
	
	/**
	 * @return the current state of the printer 
	 */
	public PrinterState getCurrentState(){
		PrinterState result = null;
		
		JSONObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = JSONUtils.createObject((JSONObject)json.get("state"), PrinterState.class.getName());
		}
		
		return result;
	}
	
	/**
	 * @param num the extruder num to check (0 indexex)
	 * @return temperature information for this extruder, null if it doesn't exist
	 */
	public TemperatureInfo getExtruderTemp(int num){
		TemperatureInfo result = null;	//may be null if num doesn't exist
		
		JSONObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			JSONObject temp = (JSONObject)json.get("temperature");
			
			//now check if this extruder exists
			if(temp.containsKey("tool" + num))
			{
				result = JSONUtils.createObject((JSONObject)temp.get("tool" + num),TemperatureInfo.class.getName());
				result.setName("Extruder " + num);
			}
		}
		
		return result;
	}
	
	/**
	 * @return temperature information for the print bed
	 */
	public TemperatureInfo getBedTemp(){
		TemperatureInfo result = null;
		
		JSONObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			JSONObject temp = (JSONObject)json.get("temperature");
			result = JSONUtils.createObject((JSONObject)temp.get("bed"),TemperatureInfo.class.getName());
			result.setName("Print Bed");
		}
		
		return result;
	}
	
	/**
	 * @param command the command to send (set temp, extrude, offset, etc)
	 * @param extruder the index of the extruder (0 indexed)
	 * @param value the value of the command (temperature, amount to extrude, etc)
	 * @return if this operation succeeded
	 */
	@SuppressWarnings("unchecked")
	public boolean sendExtruderCommand(ToolCommand command, int extruder, int value){
		boolean result = true;
		JSONObject params = null;
		
		String tool = "tool" + extruder;
		
		if(command == ToolCommand.EXTRUDE)
		{
			//we need to perform the select first
			params = new JSONObject();
			params.put("command",ToolCommand.SELECT_TOOL.getCommand());
			params.put("tool",tool);
			
			result = this.sendCommand("tool",params);
		}
		
		//catch in case select doesn't work
		if(result)
		{
			params = new JSONObject();
			
			if(command == ToolCommand.TARGET_TEMP)
			{
				JSONObject targets = new JSONObject();
				targets.put(tool,value);
				params.put("targets",targets);
			}
			else if(command == ToolCommand.TEMP_OFFSET)
			{
				JSONObject offsets = new JSONObject();
				offsets.put(tool,value);
				params.put("offsets",offsets);
			}
			else if(command == ToolCommand.EXTRUDE)
			{
				params.put("amount",value);
			}
			
			//set the command
			params.put("command",command.getCommand());
			
			result = this.sendCommand("tool",params);
		}
		
		return result;
	}
	
	/**
	 * @param command the command to send (only Target and Offset are allowed)
	 * @param value the value of the command(temp, offset)
	 * @return if this operation succeeded
	 */
	@SuppressWarnings("unchecked")
	public boolean sendBedCommand(ToolCommand command, int value){
		boolean result = false;
		
		//the bed can only use these two commands
		if(command == ToolCommand.TARGET_TEMP || command == ToolCommand.TEMP_OFFSET)
		{
			JSONObject params = new JSONObject();
			
			if(command == ToolCommand.TARGET_TEMP)
			{
				params.put("target",value);
			}
			else
			{
				params.put("offset",value);
			}
			
			params.put("command",command.getCommand());
			
			result = this.sendCommand("bed", params);
		}
		
		return result;
	}
	
	/**
	 * move all axis to the home position
	 * 
	 * @return if this command succeeded
	 */
	@SuppressWarnings("unchecked")
	public boolean moveHome(){
		OctoPrintHttpRequest request = this.createRequest("printhead");
		
		//set request type and command to send
		request.setType("POST");
		
		//add the options
		request.addParam("command", "home");
		
		JSONArray axis = new JSONArray();
		axis.add("x");
		axis.add("y");
		axis.add("z");
		
		request.addParam("axes", axis);
		
		
		return g_comm.executeUpdate(request);
	}
	
	/**
	 * Move a given axis this amount
	 * 
	 * @param axis the axis to move
	 * @param amount the amount to movie it (positive or negative)
	 * @return if this operation succeeded
	 */
	public boolean moveOnAxis(Axis axis, double amount){
		OctoPrintHttpRequest request = this.createRequest("printhead");
		
		//set request type and command to send
		request.setType("POST");
		
		//add the command
		request.addParam("command", "jog");
		request.addParam(axis.getAxis(), amount);
		
		return g_comm.executeUpdate(request);
	}
	
	public enum ToolCommand {
		TARGET_TEMP("target"),
		TEMP_OFFSET("offset"),
		SELECT_TOOL("select"),
		EXTRUDE("extrude");
		
		
		private String m_command = null;
		
		ToolCommand(String c){
			m_command = c;
		}
		
		public String getCommand(){
			return m_command;
		}
	}
}
