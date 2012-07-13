package eyevisionsearch.logic.strategies.impl.durationstrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.SetGroupEvent;
import eyevisionsearch.logic.helpers.RowContainer;
import eyevisionsearch.logic.helpers.StrategyHelper;
import eyevisionsearch.logic.strategies.StrategyException;

/**
 * container class for DurationStrategy
 * @author lkastler
 *
 */
public class DurationContainer extends RowContainer {

	protected Logger log = Logger.getLogger(DurationContainer.class);
	
	private String id;
	private String group;
	private ArrayList<Long> duration;
	
	
	/**
	 * constructor, takes EventList to create.
	 * @param el EventList to parse.
	 * @throws StrategyException thrown if specified EventList could not be parsed.
	 */
	public DurationContainer(EventList el) throws StrategyException {
		id = el.getId();
		group = "";
		duration = new ArrayList<Long>();
		
		Iterator<Event> it = el.iterator();
		
		Event groupset = StrategyHelper.getFollowingEventByType(it, "setgroup");
		if(groupset == null)
			throw new StrategyException("unexpected: no groupset event found");
		
		group = ((SetGroupEvent)groupset).getGroup();
		long buffer = 0;
		
		while(it.hasNext()) {
			Event e = (Event)it.next();
			if(e.getType().equalsIgnoreCase("next")) {
				if(((NextEvent)e).getSection().startsWith("#task")) {
					log.debug("task found @ " + e.getTime());
					buffer = e.getTime();
				}
			}
			else if(e.getType().equalsIgnoreCase("window.close")) {
				log.debug("window closed found @ " + e.getTime());
				if(buffer > 0) {
					log.debug("buffer=" + buffer);
					duration.add(e.getTime() - buffer);
					buffer = 0;
				}
				else {
					log.debug("skipped");
				}
			}
		}
		
		log.debug(Arrays.toString(duration.toArray()));
	}
	
	@Override
	public Object[] rowToArray() throws Exception {
		ArrayList<Object> obj = new ArrayList<Object>();
		
		obj.add(id);
		obj.add(group);
		obj.addAll(duration);
		
		return obj.toArray(new Object[0]);
	}
	
	/**
	 * returns group of this DurationContainer.
	 * @return  group of this DurationContainer.
	 */
	public String getGroup() {
		return group;
	}
	
	/**
	 * returns ID of this DurationContainer.
	 * @return ID of this DurationContainer.
	 */
	public String getId() {
		return id;
	}
}