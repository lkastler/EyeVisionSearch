package eyevisionsearch.logic.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eyevisionsearch.json.JSONParser;

public class EventFactory {

	private static Logger log = Logger.getLogger(EventFactory.class);
	/**
	 * parses specified JSONParser and returns an EventList.
	 * @param name name of EventList.
	 * @param parser JSONParser for EventList.
	 * @return an EventList named by specified <code>name</code>, includes Events of JSONParser.
	 * @throws EventFactoryException if something went wrong.
	 */
	public static EventList parseEvents(String name, JSONParser parser) throws EventFactoryException {
		try {
			JSONArray arr = parser.getJsonArray();
			log.info ("parsing events of " + name);
			EventList list = new EventList(name);
		
			Object current = null;
		
			for(int i = 0; i < arr.length(); ++i) {
				current = arr.get(i);
				
				if(current instanceof JSONObject) {
					try{
						list.add(parseEvent((JSONObject)current));
					}catch(EventFactoryException e) {
						log.warn(e);
					}
				}
				else
					throw new JSONException("position " + i + " is not an object!");
			}
			//log.debug(list);
			
			return list;
		} catch(JSONException e) {
			throw new EventFactoryException(("could not parse array: " + e.getMessage()), e);
		}
	}
	
	/**
	 * parses an JSONObject and returns correlated Event
	 * @param obj specified JSONObject
	 * @return correlated Event
	 * @throws EventFactoryException thrown if JSONObject could not be parsed
	 * @throws JSONException  thrown if JSONObject could not be read
	 */
	public static Event parseEvent(JSONObject obj) throws EventFactoryException, JSONException {

		if(obj.getString("type").equalsIgnoreCase("start")) {
			return parseStartEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("end")) {
			return parseEndEvent(obj);
		}
		
		else if(obj.getString("type").equalsIgnoreCase("next")) {
			return parseNextEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("precision")) {
			return parsePrecisionEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("groupset")) {
			return parseSetGroupEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("$")) {
			return parseDollarEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("top")) {
			return parseTopEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("gazeover")) {
			return parseGazeOverEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("gazeout")) {
			return parseGazeOutEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("over")) {
			return parseMouseOverEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("out")) {
			return parseMouseOutEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("intent")) {
			return parseMouseIntentEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("url")) {
			return parseUrlEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("panel.show")) {
			return parsePanelOpenEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("panel.hide")) {
			return parsePanelHideEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("image-saved")) {
			return parseImageSaveEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("panel.backup")) {
			return parsePanelBackupEvent(obj);
		}
		else if(obj.getString("type").equalsIgnoreCase("win-closed")) {
			return parseWindowClosedEvent(obj);
		}
		throw new EventFactoryException("unhandled event type: " + obj.getString("type"));
		
	}

	private static Event parsePanelHideEvent(JSONObject obj) throws JSONException {
		return new PanelHideEvent(obj.getLong("time"));
	}

	private static Event parseWindowClosedEvent(JSONObject obj) throws JSONException {
		return new WindowClosedEvent(obj.getLong("time"));
	}

	// start
	private static Event parseStartEvent(JSONObject obj)  throws EventFactoryException, JSONException  {
		log.debug("parsing start event");
		return new StartEvent(obj.getLong("time"));
	}
	
	// end
	private static Event parseEndEvent(JSONObject obj)  throws EventFactoryException, JSONException  {
		log.debug("parsing end event");
		return new EndEvent(obj.getLong("time"));	
	}
	
	// next
	// TODO more stuff
	private static Event parseNextEvent(JSONObject obj)  throws EventFactoryException, JSONException  {
		JSONObject payload = obj.getJSONObject("payload");
		
		// normal next events
		if(payload.length() == 1) {
			log.debug("parsing next event");
			return new NextEvent(obj.getLong("time"), payload.getString("$"));
		}
		
		// forced next event by main
		if(payload.length() == 0) {
			log.debug("parsing forcednext event");
			return new ForcedNextEvent(obj.getLong("time"));
		}
		
		if(payload.length() > 1) {
		
			HashMap<String, String> response = new HashMap<String, String>();
			
			String[] names = JSONObject.getNames(payload);
			log.debug("parsing next with response; keys length = " + names.length);
			for(String name : names) {
				if(!name.equalsIgnoreCase("$")) {
					log.debug(name + " = " + payload.get(name).toString());
					response.put(name, payload.get(name).toString());
				}
			}
			log.debug("next with response event parsed");
			
			return new NextWithResponsesEvent(obj.getLong("time"), payload.getString("$"), response);
		}
		
		throw new EventFactoryException("negative values in next payload are not allowed; payload: " + payload.length());
	}
	

	private static Event parseSetGroupEvent(JSONObject obj) throws JSONException {	
		log.debug("set group event parsed");
		return new SetGroupEvent(obj.getLong("time"), obj.getString("payload"));
	}
	
	private static Event parseDollarEvent(JSONObject obj) throws JSONException {
		log.debug("dollar event parsed");
		return new DollarEvent(obj.getLong("time"));
	}
	
	private static Event parsePrecisionEvent(JSONObject obj) throws JSONException {
		
		ArrayList<String> truePositives = new ArrayList<String>();
		
		JSONObject payload = obj.getJSONObject("payload");
		
		if(payload.length() > 0) {
			String[] names = JSONObject.getNames(payload);
			for(String name : names) {
				log.debug(name);
				truePositives.add(name);
			}
		}
		log.debug("precision event parsed");
		return new PrecisionEvent(obj.getLong("time"), truePositives);		
	}
	

	private static Event parseTopEvent(JSONObject obj) throws JSONException {
		log.debug("top event parsed");
		return new TopEvent(obj.getLong("time"), obj.getString("id"), obj.getJSONObject("misc").getInt("number"));
	}
	
	private static Event parseGazeOverEvent(JSONObject obj) throws JSONException {
		log.debug("gaze over event parsed");
		if(!obj.isNull("id"))
			return new GazeOverEvent(obj.getLong("time"), obj.getString("id"));
		return new GazeOverEvent(obj.getLong("time"), "no id");
		
	}
	
	private static Event parseGazeOutEvent(JSONObject obj) throws JSONException {
		log.debug("gaze out event parsed");
		if(!obj.isNull("id"))
			return new GazeOutEvent(obj.getLong("time"), obj.getString("id"));
		return new GazeOutEvent(obj.getLong("time"), "no id");
	}

	
	private static Event parseMouseOverEvent(JSONObject obj) throws JSONException {
		log.debug("mouse over event parsed");
		if(!obj.isNull("id"))
			return new MouseOverEvent(obj.getLong("time"), obj.getString("id"));
		return new MouseOverEvent(obj.getLong("time"), "no id");
	}
	

	private static Event parseMouseOutEvent(JSONObject obj) throws JSONException {
		log.debug("mouse out event parsed");
		if(!obj.isNull("id"))
			return new MouseOutEvent(obj.getLong("time"), obj.getString("id"));
		return new MouseOutEvent(obj.getLong("time"), "no id");
	}
	
	private static Event parseMouseIntentEvent(JSONObject obj) throws JSONException {
		log.debug("mouse intent event parsed");
		if(!obj.isNull("id"))
			return new MouseIntentEvent(obj.getLong("time"), obj.getString("id"));
		return new MouseIntentEvent(obj.getLong("time"), "no id");
	}
	
	private static Event parseUrlEvent(JSONObject obj) throws JSONException {
		if(!obj.isNull("id"))
			return new UrlEvent(obj.getLong("time"), obj.getString("id"));
		return new UrlEvent(obj.getLong("time"), "no url");
	}
	
	private static Event parseImageSaveEvent(JSONObject obj) throws JSONException {
		return new ImageSaveEvent(obj.getLong("time"), obj.getString("id"));
	}
	
	private static Event parsePanelOpenEvent(JSONObject obj) throws JSONException {
		return new PanelOpenEvent(obj.getLong("time"));
	}
	
	private static Event parsePanelBackupEvent(JSONObject obj) throws JSONException {
		if(!obj.isNull("data"))
			// FIXME: PanelBackup doesn't include timestamp!
			return new PanelBackupEvent(0, obj.getJSONObject("data"));
		return new PanelBackupEvent(0);
	}
}
