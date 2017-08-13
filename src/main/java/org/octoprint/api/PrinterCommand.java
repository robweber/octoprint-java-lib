package org.octoprint.api;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.octoprint.api.model.Axis;
import org.octoprint.api.model.PrinterState;
import org.octoprint.api.model.TemperatureInfo;
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

	private boolean sendCommand(String path, JsonObject payload){
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
		
		JsonObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			result = JSONUtils.createObject((JsonObject)json.get("state"), PrinterState.class.getName());
		}
		
		return result;
	}
	
	/**
	 * @param num the extruder num to check (0 index)
	 * @return temperature information for this extruder, null if it doesn't exist
	 */
	public TemperatureInfo getExtruderTemp(final int num){
		TemperatureInfo result = null;	//may be null if num doesn't exist
		
		JsonObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			JsonObject temp = (JsonObject)json.get("temperature");
			
			//now check if this extruder exists
			if(temp.containsKey("tool" + num))
			{
				result = JSONUtils.createObject((JsonObject)temp.get("tool" + num),TemperatureInfo.class.getName());
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
		
		JsonObject json = this.g_comm.executeQuery(this.createRequest());
		
		if(json != null)
		{
			JsonObject temp = (JsonObject)json.get("temperature");
			result = JSONUtils.createObject((JsonObject)temp.get("bed"),TemperatureInfo.class.getName());
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
	public boolean sendExtruderCommand(ToolCommand command, final int extruder, final int value){
		boolean result = true;
		JsonObject params = null;
		
		String tool = "tool" + extruder;
		
		if(command == ToolCommand.EXTRUDE)
		{
			//we need to perform the select first
			params = new JsonObject();
			params.put("command",ToolCommand.SELECT_TOOL.getCommand());
			params.put("tool",tool);
			
			result = this.sendCommand("tool",params);
		}
		
		//catch in case select doesn't work
		if(result)
		{
			params = new JsonObject();
			
			if(command == ToolCommand.TARGET_TEMP)
			{
				JsonObject targets = new JsonObject();
				targets.put(tool,value);
				params.put("targets",targets);
			}
			else if(command == ToolCommand.TEMP_OFFSET)
			{
				JsonObject offsets = new JsonObject();
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
	public boolean sendBedCommand(ToolCommand command, final int value){
		boolean result = false;
		
		//the bed can only use these two commands
		if(command == ToolCommand.TARGET_TEMP || command == ToolCommand.TEMP_OFFSET)
		{
			JsonObject params = new JsonObject();
			
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
	public boolean moveHome(){
		OctoPrintHttpRequest request = this.createRequest("printhead");
		
		//set request type and command to send
		request.setType("POST");
		
		//add the options
		request.addParam("command", "home");
		
		JsonArray axis = new JsonArray();
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
	public boolean moveOnAxis(Axis axis, final double amount){
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
