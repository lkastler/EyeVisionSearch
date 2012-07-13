package eyevisionsearch.logic.strategies.impl.precisionstrategy;

import java.util.ArrayList;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.PanelBackupEvent;
import eyevisionsearch.logic.events.PrecisionEvent;
import eyevisionsearch.logic.events.SetGroupEvent;
import eyevisionsearch.logic.helpers.RowContainer;

public class PrecisionContainer extends RowContainer {
	
	private ArrayList<PrecisionHelper> values;
	private String id;
	public String getId() {
		return id;
	}

	private String group;
	
	public PrecisionContainer(EventList el) {
		values = new ArrayList<PrecisionHelper>();
		
		PrecisionHelper ph = null;
		
		id = el.getId();
		for(Event e: el) {
			if(e.getType().equalsIgnoreCase("setgroup")) {
				group = ((SetGroupEvent)e).getGroup();
			}
			else if(e.getType().equalsIgnoreCase("panel.backup")) {
				ph = new PrecisionHelper(((PanelBackupEvent)e).getRankings());
			}
			else if(e.getType().equalsIgnoreCase("precision")) {
				if(ph != null) {
					ph.addTruePosivitves(((PrecisionEvent)e).getTruePositives());
					values.add(ph);
					ph = null;
				}
			}
			
		}
	}
	
	@Override
	public Object[] rowToArray() throws Exception {
		ArrayList<Object> o = new ArrayList<Object>();
		o.add(id);
		o.add(group);
		for(PrecisionHelper ph : values) {
			o.add(values.indexOf(ph) + 1);
			o.add(ph.getPositivesSize());
			o.add(ph.getTruePositivesSize());
			o.add(ph.getHitsWithin());
			o.add(ph.getHitsWithin(10));
			o.add(ph.getHitsWithin(20));
		}
		
		return o.toArray();
	}

	public String getGroup() {
		return group;
	}

}
