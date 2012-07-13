package eyevisionsearch.logic.strategies.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.PanelBackupEvent;
import eyevisionsearch.logic.events.PrecisionEvent;
import eyevisionsearch.logic.helpers.DualValue;
import eyevisionsearch.logic.helpers.StrategyHelper;
import eyevisionsearch.logic.strategies.Strategy;

public class AlgorithmEvaluationStrategy extends Strategy {

	public AlgorithmEvaluationStrategy() {
		super();
	}

	private class PrecisionHandler {
		
		private String id;
		private HashMap<String, DualValue<PanelBackupEvent, PrecisionEvent>> precision;
		
		
		public PrecisionHandler(EventList el) {
			id = el.getId();
			precision = new HashMap<String, DualValue<PanelBackupEvent, PrecisionEvent>>();
			
			String group = StrategyHelper.getGroup(el);
			
			if(!group.equalsIgnoreCase("a")) {
				
				String task = null;
				PanelBackupEvent pb = null;
				
				Iterator<Event> it = el.iterator();
				
				while(it.hasNext()) {
					
					Event e = it.next();
					
					// task section passed
					if(e.getType().equalsIgnoreCase("next") && ((NextEvent)e).getSection().startsWith("#task")) {
						task = ((NextEvent)e).getSection(); 
					}
					
					// panel backup passed
					else if(e.getType().equalsIgnoreCase("panel.backup") && task != null) {
						pb = (PanelBackupEvent)e;
					}
					
					// precision passed
					else if(e.getType().equalsIgnoreCase("precision") && task != null && pb != null) {
						if (pb.getSize() > 0)
							precision.put(task, new DualValue<PanelBackupEvent, PrecisionEvent>(pb, (PrecisionEvent)e));
						else
							precision.put(task, null);
						
						task = null;
						pb = null;
					}
					
				}
			}
			else {
				precision.put("group A", null);
			}
			
		}
		
		public ArrayList<Object[]> getPrecisions() {
			ArrayList<Object[]> list = new ArrayList<Object[]> ();
				
			for(String task : precision.keySet()) {
				Object[] s = new Object[5];
				s[0] = id;
				s[1] = task;
				
				if(precision.get(task) != null) {
					s[2] = precision.get(task).getFirst().getSize();
					s[3] = precision.get(task).getLast().getTruePositives().size();
					
					if(precision.get(task).getFirst().getSize() > 0)
						s[4] = (double)precision.get(task).getLast().getTruePositives().size() 
								/ (double)precision.get(task).getFirst().getSize();
					else
						s[4] = 0d;
				}
				else {
					s[2] = 0d;
					s[3] = 0d;
					s[4] = 0d;
				}
				
				
				list.add(s);
 			}
			
			return list;
		}
	}

	
	
	
	
	@Override
	public TableModel compile() throws Exception {
		
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		for(EventList el : list) {
			obj.addAll(new PrecisionHandler(el).getPrecisions());
		}
		
		
		Object[] cols = {"Participant", "Task", "# positive", "# true positive","Precision"};
		
		TableModel t = new DefaultTableModel(obj.toArray(new Object[0][0]), cols); 
		
		return t;
	}

	@Override
	public String getName() {
		return "Algorithm Evaluation Strategy";
	}

}
