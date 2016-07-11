package org.octoprint.api;

public enum Axis {
	X("x"),
	Y("y"),
	Z("z");
	
	private String m_axis;
	Axis(String a){
		m_axis = a;
	}
	
	public String getAxis(){
		return m_axis;
	}
	
	/**
	 * A helper method to convert a passed in string value to an Axis enum 
	 * 
	 * @param t axis name as a string
	 * @return conversion of string to enum value
	 */
	public static Axis getAxis(String t){
		if(t.toLowerCase().equals("x"))
		{
			return Axis.X;
		}
		else if(t.toLowerCase().equals("y"))
		{
			return Axis.Y;
		}
		else if(t.toLowerCase().equals("z"))
		{
			return Axis.Z;
		}
		
		return null;
	}
}
