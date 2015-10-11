package Cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Document extends ServerResource {
	String fileName = "";
	Logger logger = Logger.getLogger("MyLog");
	FileHandler fh;
	
	@Get
    public FileRepresentation getResource() throws IOException {
		FileRepresentation result = null;
		Request request = getRequest();
		Form form = request.getResourceRef().getQueryAsForm();
		
		//Set up log
		try{
			 fh = new FileHandler("fileLog.log");
			}catch(SecurityException | IOException e){
				e.printStackTrace();
			}   	
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(Level.INFO);
		logger.setUseParentHandlers(false);
		
		if(form.getValues("fileName") != null)
		{
			fileName += form.getValues("fileName");
		}
		for (int i = 0; i<Main.listOfCachedFiles.size(); i++)
    	{
    		if((Main.filePath+"/"+fileName).equals(Main.listOfCachedFiles.get(i).getName().toString()))
    		{
    			Logger.getLogger("MyLog").info("user request: file " + fileName + " at " + Main.getDate() 
    			+ System.lineSeparator() + "response: cached file " + fileName);  			
    			return new FileRepresentation(Main.filePath + "//" + fileName, MediaType.TEXT_HTML);
    		} 
    	}

		ClientResource file = new ClientResource("http://localhost:" + Main.serverPort + "/api/file?filename="+fileName);
		Logger.getLogger("MyLog").info("user request: file " + fileName + " at " + Main.getDate() 
		+ System.lineSeparator() + "response:file " + fileName + " downloaded from the server"); 
		Representation response = file.get();
		InputStream fileInput = response.getStream();
		OutputStream fileOut = new FileOutputStream(new File(Main.filePath+"/"+fileName));
		int read = 0;
		byte[] bytes = new byte[1024];
		 while ((read = fileInput.read(bytes)) != -1)
		 {
			 fileOut.write(bytes, 0, read);
		 }
		 fileInput.close();
		 fileOut.flush();
		 fileOut.close();
		 Main.listOfCachedFiles.add(new File(Main.filePath+"/"+fileName));
		 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.TEXT_HTML);
		 return result;
    }
}
