# Octoprint Java Lib
This is a very simple wrapper to provide Java based communication to an OctoPrint Server using the REST API (http://docs.octoprint.org/en/master/api/)

Please note that not all API functions are implemented here but most of them for doing common operations are ready to go. Pull Requests for additional functionality are welcome. 

## Required Libs

The json-simple library is required to encode/decode JSON objects. If you don't want to use maven here is a link: https://github.com/fangyidong/json-simple

## How to Use It

To use it simply create an OcotPrintInstance within your Java code and then use it to initate one of the Command classes.

```java

OctoPrintInstance octoprint = new OctoPrintInstance("octoprint.local",80,"api_key");
PrinterCommand printer = new PrinterCommand(octoprint);

//get the current state
System.out.println(printer.getCurrentState());

if(printer.getCurrentState().isReady())
{
  //set the extruder temp - use the ToolCommand enum to get the command,the extruder num (0 indexed) and the value
  printer.sendExtruderCommand(ToolCommand.TARGET_TEMP,0,220);
}

```
