package eyevisionsearch.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * parses JSON and gives the JSONObject back. 
 * 
 * @author lkastler
 *
 */
public class JSONParser {
	public static enum Type {JsonObject, JsonArray};

	private Logger log = Logger.getLogger(JSONParser.class);
	
	private Object obj;
	private Type type;
	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws JSONException
	 */
	public JSONParser(File file) throws JSONException, FileNotFoundException {
		this(new JSONTokener(new FileReader(file)));		
	}
	
	/**
	 * 
	 * @param fileName
	 * @throws JSONException
	 */
	public JSONParser(String str) throws JSONException {
		this(new JSONTokener(str));
		
	}
	
	private JSONParser(JSONTokener tok) throws JSONException {
		char test = tok.nextClean();
		tok.back();
		switch(test){
			case '[':
				type = Type.JsonArray;
				log.debug("JSON array successfully parsed");
				obj = new JSONArray(tok);
				return;
			case '{':
				type = Type.JsonObject;
				log.debug("JSON object successfully parsed");
				obj = new JSONObject(tok);
				return;
		}
		
		throw new JSONException("not JSON array or JSON object");
	}

	/**
	 * returns the JSONObject parsed by this JSONParser
	 * @return the JSONObject parsed by this JSONParser
	 * @throws JSONException 
	 */
	public JSONObject getJsonObject() throws JSONException {
		if(type == Type.JsonObject)
			return (JSONObject)obj;
		else
			throw new JSONException("JSONParser parsed no JSONObject");
	}
	
	public JSONArray getJsonArray() throws JSONException {
		if(type == Type.JsonArray)
			return (JSONArray)obj;
		else
			throw new JSONException("JSONParser parsed no JSONArray");
	}
	
	/**
	 * returns the type of contained JSON something
	 * @return
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * String representation of JSON
	 */
	public String toString() {
		if(type == Type.JsonArray)
			return ((JSONArray)obj).toString();
		if(type == Type.JsonObject)
			return ((JSONObject)obj).toString();
		return "JSONParser[empty]";
	}
}

