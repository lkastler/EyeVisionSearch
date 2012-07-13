package eyevisionsearch;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import eyevisionsearch.json.JSONParser;
import eyevisionsearch.json.JsonConfig;
import eyevisionsearch.logic.JsonFileFilter;
import eyevisionsearch.logic.events.EventFactory;
import eyevisionsearch.logic.events.EventFactoryException;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.StrategyProvider;
import eyevisionsearch.opendocument.OpenDocument;

/**
 * main for EyeVisionSearch
 * @author lkastler
 *
 */
public class Main {
	static Logger log = Logger.getLogger(Main.class);

	private File input;
	private File output;
	private ArrayList<Strategy> strats;
	
	/**
	 * constructor that parses a JSON file, specified by given fileName.
	 * @param fileName file name of JSON configuration file.
	 * @throws Exception thrown if something went wrong
	 */
	public Main(String fileName) throws Exception {
		JsonConfig config = JsonConfig.parseFile(new File(fileName));
		
		input = new File((String)config.get("input"));
		
		log.info("input: " + input.getAbsolutePath());
		
		output = new File((String)config.get("output"));
		
		log.info("output: " + output.getAbsolutePath());
		
		strats = new ArrayList<Strategy>();

		JSONArray arr = (JSONArray)config.get("strategies");
		
		for(int i = 0; i < arr.length(); ++i)
			strats.add(StrategyProvider.getInstance().loadStrategy(arr.getString(i)));
		
		StringBuffer buf = new StringBuffer("Strategies: {");
		
		for(Strategy cur : strats) {
			buf.append("[" + cur.getName() + "]");
		}
		buf.append("}");
		
		log.info(buf.toString());
	}
	
	/**
	 * executes the configured behavior of this Main
	 * @throws Exception thrown if something went wrong
	 */
	public void execute() throws Exception {
		log.info("execute");
		
		execute(input, strats, output);
	}
	
	/**
	 * parses specified List of File to an ArrayList of EventLists.
	 * @param files files to parse.
	 * @return ArrayList of EventLists parsed of the specified files.
	 */
	private static ArrayList<EventList> parseFiles(List<File> files) {
		ArrayList<EventList> eventLists = new ArrayList<EventList>();
		
		for(File cur : files) {
			try {
				eventLists.add(EventFactory.parseEvents(cur.getParentFile().getName(), new JSONParser(cur)));
			} catch(JSONException e) {
				log.warn(cur.getPath() + " : could not be parsed: " + e.getMessage());
			} catch (FileNotFoundException e) {
				log.warn(cur.getPath() + " : " + e.getMessage());
			} catch (EventFactoryException e) {
				log.warn(cur.getPath() + ":" + e.getMessage());
			}
		}
		return eventLists;
	}
	
	private static ArrayList<File> collectJsonFiles(List<File> files) {
		ArrayList<File> output = new ArrayList<File>();
		
		for(File cur : files) {
			if(JsonFileFilter.instance().accept(cur)) {
				if(cur.isDirectory()) {
					output.addAll(
							collectJsonFiles(
									Arrays.asList(
											cur.listFiles(
													JsonFileFilter.instance()))));
				}
				else {
					assert(cur.getName().endsWith(".json"));
					
					output.add(cur);
				}
			}
		}
		
		return output;
	}
	
	/**
	 * do your dirty work! executes specified strategy on specified List of JSON files and 
	 * saves the result as OpenDocument SpreadSheet in specified odFile
	 * 
	 * @param files List of JSON files to parse.
	 * @param strategies strategies that should be applied on parsed JSON files
	 * @param odFile file name to save the result of specified strategies
	 * @throws Exception thrown if anything went wrong
	 */
	public static void execute(List<File> files, List<Strategy> strategies, File odFile) throws Exception {
		log.info("collecting files");
		
		ArrayList<File> jsonFiles = collectJsonFiles(files);
		
		log.info("done. " + jsonFiles.size() + " files found");
		
		log.info("parsing files");
		
		ArrayList<EventList> eventLists = parseFiles(jsonFiles);
		
		OpenDocument od = new OpenDocument();
		
		for(Strategy strategy : strategies) {
			log.info("execute strategy: " + strategy.getName());
			
			strategy.setList(eventLists);
			
			od.addSheet(strategy.getName(), strategy.compile());
		}
		
		log.info("generate open document file: " + odFile.getPath());
	
		od.saveAs(odFile);
		
		log.info("done");
	}
	
	/**
	 * parses either files in <code>dir</code if directory or the file specified by <code>dir</code> 
	 * and creates a OpenDocument Spreadsheet specified by <code>odFile</code> using Strategies specified by <code>strategies</code>. 
	 * 
	 * @param dir either single file or directory
	 * @param strategies strategies that should be applied on parsed JSON files
	 * @param odFile odFile file name to save the result of specified strategies
	 * @throws Exception thrown if anything went wrong
	 */
	public static void execute(File dir, List<Strategy> strategies, File odFile) throws Exception {
		ArrayList<File> list = new ArrayList<File>();
		
		if(dir.isDirectory()) {
			list.addAll(Arrays.asList(dir.listFiles(JsonFileFilter.instance())));
		}
		else {
			list.add(dir);
		}
		
		execute(list, strategies, odFile);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String config;
		if(args.length > 0) {
			log.info("configuration file: " + args[0]);
			config  = args[0];
		} else {
			log.warn("no configuation file specified");
			log.warn("tries to use default config file");
			
			config = "config.json";
		}
		try {
			long time = System.currentTimeMillis();
			log.info("begin");
				
			new Main(config).execute();
				
			log.info("end");
			log.info("duration: " + (System.currentTimeMillis() -time) + " ms");
			
		} catch (Exception e) {
			log.error(e);
		}
	}
}
