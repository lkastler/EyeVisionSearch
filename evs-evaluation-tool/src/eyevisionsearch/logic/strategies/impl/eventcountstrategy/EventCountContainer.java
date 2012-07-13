package eyevisionsearch.logic.strategies.impl.eventcountstrategy;

import java.util.ArrayList;
import java.util.Arrays;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.SetGroupEvent;
import eyevisionsearch.logic.helpers.RowContainer;

public class EventCountContainer extends RowContainer {

	private String id;
	private String group;
	private ArrayList<EventCountData> taskData;
		
	public EventCountContainer(EventList el) {
		id = el.getId();
		taskData = new ArrayList<EventCountData>();
		EventCountData data = null;
		
		for(Event e: el) {
			if(data != null) {
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
			
			if(e.getType().contains("next")) {
				if(((NextEvent)e).getSection().startsWith("#task")) {
					data = new EventCountData();
					
					// since 1st request is not stored as event, we need to add it up
					data.addUrlEventOccur();
					
					if(group.equalsIgnoreCase("A")) {
						data.disableSpecialEvents();
					}
					
					taskData.add(data);

				}
			}
			
			else if(e.getType().contains("setgroup")) {
				group = ((SetGroupEvent)e).getGroup();
			}
			
		}
	}

	
	
	@Override
	public Object[] rowToArray() throws Exception {
		ArrayList<Object> obj = new ArrayList<Object>();
		
		obj.add(id);
		obj.add(group);
		for(EventCountData task :taskData) {
			obj.add(taskData.indexOf(task) + 1);
			obj.addAll(Arrays.asList(task.toArray()));
		}
		
		return obj.toArray();
	}

	public String getId() {
		return id;
	}
	
	public String getGroup() {
		return group;
	}


}
