package eyevisionsearch.logic.strategies.impl.eventcountstrategy;

import java.util.ArrayList;
import java.util.Arrays;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.SetGroupEvent;
import eyevisionsearch.logic.helpers.RowContainer;

/**
 * container counts all special events i.e. mouse or gaze events. 
 * @author lkastler
 *
 */
public class FullEventCountContainer extends RowContainer {
	private String id;
	private String group;
	private EventCountData data;
	private int tasks;
	
	/**
	 * constructor, takes specified EventList and counts events.
	 * @param el EventList for event count.
	 */
	public FullEventCountContainer(EventList el) {
		id = el.getId();
		group = "";
		data = new EventCountData();
		tasks = 0;
		
		
		for(Event e: el) {
			if(e.getType().contains("next")) {
				if(((NextEvent)e).getSection().startsWith("#task")) {
					tasks++;
				}
			}
			else if(e.getType().contains("setgroup")) {
				group = ((SetGroupEvent)e).getGroup();
				if(group.equalsIgnoreCase("A")) {
					data.disableSpecialEvents();
				}
			}
			
			if(tasks > 0) {
				if(e.getType().contains("mouse"))
					data.addMouseEventOccur();
				else if(e.getType().contains("gaze.over"))
					data.addGazeEventOccur();
				else if(e.getType().contains("url"))
					data.addUrlEventOccur();
				else if(e.getType().contains("panel.open"))
					data.addPanelOpenEventOccur();
				else if(e.getType().contains("imageSave"))
					data.addImageSaveEventOccur();
			}
		}
	}
	
	

	@Override
	public Object[] rowToArray() throws Exception {
		
		ArrayList<Object> obj = new ArrayList<Object>();
		
			
		obj.add(id);
		obj.add(group);
		obj.addAll(Arrays.asList(data.toArray()));
		obj.add(tasks);
				
		return obj.toArray();
	}

	/**
	 * returns the ID stored in this EventCountContainer.
	 * @return the ID stored in this EventCountContainer.
	 */
	public String getId() {
		return id;
	}


	/**
	 * returns the group stored in this EventCountContainer.
	 * @return the group stored in this EventCountContainer.
	 */
	public String getGroup() {
		return group;
	}
}
