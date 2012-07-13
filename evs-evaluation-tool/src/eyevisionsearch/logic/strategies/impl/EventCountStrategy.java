package eyevisionsearch.logic.strategies.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.impl.eventcountstrategy.EventCountContainer;

public class EventCountStrategy extends Strategy {

	public EventCountStrategy(){}

	@Override
	public TableModel compile() throws Exception {
		ArrayList<EventCountContainer> cont = new ArrayList<EventCountContainer>(); 
		for(EventList el: list) {
			cont.add(new EventCountContainer(el));
		}
		
		Collections.sort(cont, new Comparator<EventCountContainer>() {

			@Override
			public int compare(EventCountContainer o1,
					EventCountContainer o2) {
				if(o1.getGroup().equalsIgnoreCase(o2.getGroup())) {
					return o1.getId().compareToIgnoreCase(o2.getId());
				}
				return o1.getGroup().compareToIgnoreCase(o2.getGroup());
			}
			
		});
		
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		for(EventCountContainer c : cont) {
			data.add(c.rowToArray());
		}
		
		ArrayList<String> cols = new ArrayList<String>();
		
		cols.add("P");
		cols.add("g");
		
		for(int i = 0; i < 6; ++i) {
			cols.add("task");
			cols.add("mouse");
			cols.add("gaze");
			cols.add("url");
			cols.add("saved w/");
			cols.add("panel opened");
		}
		
		
		return new DefaultTableModel(data.toArray(new Object[0][0]), cols.toArray());
	}

	@Override
	public String getName() {
		return "Event Count Strategy";
	}
}
