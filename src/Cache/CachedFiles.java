package Cache;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CachedFiles extends ServerResource{
	
	@Get
    public JsonRepresentation getResource() throws JSONException {
		JSONArray list =  new JSONArray();
		ArrayList<File> files= Main.listOfCachedFiles;
		for(File file : files)
		{
			list.put(file.getName());
		}		
		return new JsonRepresentation(list);		
	}

}
