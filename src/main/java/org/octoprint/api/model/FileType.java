package org.octoprint.api.model;

public enum FileType {
	MODEL,MACHINECODE,FOLDER;
	
	@Override
	public String toString(){
		return this.name().toLowerCase();
	}
	
	public static FileType findType(String t){
		FileType result = null;
		
		if(t.equals(FileType.FOLDER.toString()))
		{
			result = FileType.FOLDER;
		}
		else if(t.equals(FileType.MACHINECODE.toString()))
		{
			result = FileType.MACHINECODE;
		}
		else
		{
			result = FileType.MODEL;
		}
		
		return result;
	}
}
