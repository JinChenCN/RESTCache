package Cache;

import java.io.File;

import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ClearCache extends ServerResource{
	
	@Get
    public StringRepresentation clearResource() {
		StringRepresentation result = null;
		if(Delete(Main.filePath))
		{
			result = new StringRepresentation("All cached files have been cleared!");
		}else
		{
			result = new StringRepresentation("There was something wrong, please try again later!");
		}
			
		return result;	
	}
	
	private Boolean Delete(String filePath)
	{
		Boolean result = false;
		File folder = new File(filePath);
		if(folder.listFiles() != null)
		{
			 for(File cachedFile : folder.listFiles()) {
				 cachedFile.delete();               
		        }
			 if(folder.list().length == 0)
			 {
				 result = true;
			 }
		}
		else
		{
			result = true;
		}	
		 return result;
	}

}
