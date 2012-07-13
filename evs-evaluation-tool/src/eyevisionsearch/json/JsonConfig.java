package eyevisionsearch.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * handles JSON files for configuration purposes.
 * 
 * The config file has to contain a JSON object serialization.
 * All properties of this object will be converted to a string-object map
 * 
 * @author lkastler
 */
public class JsonConfig implements Iterable<String>{
	
	protected static Logger log = Logger.getLogger(JsonConfig.class);
	
	private HashMap<String, Object> map;
	
	/**
	 * creates a JsonConfig from specified file.
	 * @param file config file serializing a JSON object
	 * @return
	 * @throws FileNotFoundException
	 * @throws JSONException
	 */
	public static JsonConfig parseFile(File file) throws FileNotFoundException, JSONException {
		
		log.debug("parsing: " + file.getAbsolutePath());
		
		JSONObject conf = new JSONObject(new JSONTokener(new FileReader(file))); 
		HashMap<String, Object> map = new HashMap<String, Object>(conf.length());
		
		for(String cur : JSONObject.getNames(conf)) {
			map.put(cur, conf.get(cur));
		}
		
		log.debug("success");
		
		return new JsonConfig(map);
	}
	
	/**
	 * constructor, takes a String-Object HashMap as configuration
	 * 
	 * @param map
	 */
	private JsonConfig(HashMap<String, Object> map) {
		this.map = map;
	}

	/**
	 * returns the Object specified by given key, retuns <code>null</code> if no object is defined by key
	 * @param key key to request the object
	 * @return Object specified by given key or <code>null</code> if key is not specified
	 */
	public Object get(String key) {
		return map.get(key);
	}
	
	@Override
	public Iterator<String> iterator() {
		return map.keySet().iterator();
	}
	
	
}
