package eyevisionsearch.logic.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.SetGroupEvent;

/**
 * collection of helper functions for Strategy based classes.
 * @author lkastler
 *
 */
abstract public class StrategyHelper {

	/**
	 * extracts the group of a participant from given Event Iterator.
	 * @param it Event Iterator for whom the group should be extracted.
	 * @return string representation of group or empty string if group event was not found.
	 */
	public static String getGroup(Iterator<Event> it) {
		SetGroupEvent e = (SetGroupEvent)getFollowingEventByType(it, "setgroup");
		if(e != null)
			return e.getGroup();
		return "";
	}
	
	/**
	 * extracts the group of a participant from given EventList.
	 * @param el EventList from whom the group shold be extracted.
	 * @returnstring representation of group or empty string if group event was not found.
	 */
	public static String getGroup(EventList el) {
		return getGroup(el.iterator());
	}
	
	/**
	 * returns a List of Events with the specified type from specified EventList.
	 * @param el EventList from that all events specified by type should be extrated.
	 * @param type type of events that should be extracted.
	 * @return
	 */
	public static List<Event> getAllEventsByType(EventList el, String type){
		ArrayList<Event> x = new ArrayList<Event>();
		
		Iterator<Event> it = el.iterator();
		Event e;
		while((e = getFollowingEventByType(it, type))!= null)
			x.add(e);
		return x;
	}
	
	/**
	 * returns the next Event. specified by given type, from the Event Iterator. 
	 * If Event was found, the cursor of given Event Iterator is placed on this.
	 * If no Event was found, the cursor is place on the end of Event Iterator.
	 * @param it Event Iterator where the requested event is referenced.
	 * @param type type of desired Event
	 * @return returns the Event or null, if event was not found
	 */
	public static Event getFollowingEventByType(Iterator<Event> it, String type) {
		Event e;
		while(it.hasNext()) {
			e = it.next();
			
			if(e.getType().equalsIgnoreCase(type))
				return e;
		}
		return null;
	}
}
