package eyevisionsearch.logic.strategies.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.impl.eventcountstrategy.FullEventCountContainer;

public class FullEventCountStrategy extends Strategy {
	
	public FullEventCountStrategy() {
		super();
	}
	
	@Override
	public TableModel compile() throws Exception {
		log.debug("list size = " + list.size());
		
		ArrayList<FullEventCountContainer> buff = new ArrayList<FullEventCountContainer>(list.size());
		
		for(EventList el : list)
			buff.add(new FullEventCountContainer(el));
				
		Collections.sort(buff, new Comparator<FullEventCountContainer>() {

			// sorts EventContContainers by group and id
			@Override
			public int compare(FullEventCountContainer o1, FullEventCountContainer o2) {
				if(!o1.getGroup().equalsIgnoreCase(o2.getGroup())) {
					return o1.getGroup().compareToIgnoreCase(o2.getGroup());
				}
				return o1.getId().compareToIgnoreCase(o1.getId()); 
			}
			
		});
		
		ArrayList<Object[]> obj = new ArrayList<Object[]>(buff.size());
		
		for(FullEventCountContainer c : buff) {
			obj.add(c.rowToArray());
		}
		
		Object[] col = {"participant", "group", "mouse events", "gaze events", "url events", "panel opened", "saved w/", "tasks"};
		TableModel t = new DefaultTableModel(obj.toArray(new Object[0][0]),col);
		
		return t;
	}

	@Override
	public String getName() {
		return "Full Event Count Strategy";
	}

}
