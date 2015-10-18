package Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Main {
	static Integer port = 8084;
	static Integer serverPort = 8083;
	static String filePath = "";
	static ArrayList<File> listOfCachedFiles = new ArrayList<File>();
	static ArrayList<String> ServerList = new ArrayList<String>();
    static File logName = new File("log.log");
	
	public static void main(String[] args) throws Exception {  		
		getProperties(args[0]);
		// Initiate cached Files
		File cachedFilefolder = new File(filePath);
		if(cachedFilefolder.listFiles()!=null)
		{
			for(File cachedfile : cachedFilefolder.listFiles())
			{
				cachedfile.delete();   
			}
		}
			
    	getFileList();   	
    	
    	// Initiate log
    	FileWriter writer = new FileWriter(logName, false);
    	PrintWriter printWriter = new PrintWriter(writer, false);
    	printWriter.flush();
    	printWriter.close();
    	writer.close();
    	
	    // Create a new Component.  
	    Component component = new Component(); 

	    // Add a new HTTP server listening on port configured, the default port is 8184.  
	    component.getServers().add(Protocol.HTTP, port); 
	    
	    component.getClients().add(Protocol.HTTP);

	    // Attach the application.  
	    component.getDefaultHost().attach("/api",  
	            new APISever());  

	    // Start the component.  
	    component.start();	
	   
	} 
	
	private static void getProperties(String configFilePath){
		Properties configFile = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream(configFilePath);
			configFile.load(file);
			file.close();
			port = Integer.parseInt(configFile.getProperty("CachePort"));
			serverPort = Integer.parseInt(configFile.getProperty("ServerPort"));
			filePath = configFile.getProperty("CacheFilePath");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getFileList() throws JSONException
	{
		try {
			ClientResource list = new ClientResource("http://localhost:" +serverPort + "/api/files");
			Representation result = list.get();
			JsonRepresentation jsonRepresentation = new JsonRepresentation(result);			
			JSONArray array = jsonRepresentation.getJsonArray();
			for(int i =0; i<array.length(); i++)
			{
				ServerList.add(array.get(i).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static String getDate()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss yyyy-MM-dd");     
		return sDateFormat.format(new java.util.Date()); 
	}
}
