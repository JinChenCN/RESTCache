package Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Main {
	static Integer port = 8184;
	static Integer serverPort = 8183;
	static String filePath = "";
	static ArrayList<File> listOfCachedFiles = new ArrayList<File>();
	static ArrayList<String> ServerList = new ArrayList<String>();
	final static Logger logger = Logger.getLogger(Main.class.getName());
	static FileHandler fh = null; 
	
	public static void main(String[] args) throws Exception {  		
		getProperties(args[0]);
		File folder = new File(filePath);
		for(File file : folder.listFiles())
		{
			listOfCachedFiles.add(file);
		}
    	
    	//Set up log
    	try{
    	 fh = new FileHandler(filePath + "//fileLog.log", false);
    	}catch(SecurityException | IOException e){
    		e.printStackTrace();
    	}   	
    	Logger fileLog = Logger.getLogger("");
    	fh.setFormatter(new SimpleFormatter());
    	fileLog.addHandler(fh);
    	fileLog.setLevel(Level.INFO);
    	
    	getFileList();
    	
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
