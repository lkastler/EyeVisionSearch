package eyevisionsearch.logic;

import java.io.File;
import java.io.FileFilter;

public class JsonFileFilter implements FileFilter {

	private static JsonFileFilter instance = null;
	
	public static JsonFileFilter instance() {
		if(instance == null)
			instance = new JsonFileFilter();
		return instance;
	}
	
	private JsonFileFilter() {
	}
	
	@Override
	public boolean accept(File file) {
		if(file.isDirectory())
			return true;
		
		if(file.getName().endsWith(".json"))
			return true;
		
		return false;
	}
}
