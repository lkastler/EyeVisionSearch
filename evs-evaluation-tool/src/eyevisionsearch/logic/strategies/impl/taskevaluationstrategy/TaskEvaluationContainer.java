package eyevisionsearch.logic.strategies.impl.taskevaluationstrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.NextWithResponsesEvent;
import eyevisionsearch.logic.helpers.LikertScaleFivePoint;
import eyevisionsearch.logic.helpers.MultipleRowContainer;
import eyevisionsearch.logic.helpers.StrategyHelper;

public class TaskEvaluationContainer extends MultipleRowContainer {

	private static Logger log = Logger.getLogger(TaskEvaluationContainer.class);
	
	HashMap<String, Task> ts;
	
	public TaskEvaluationContainer(List<EventList> list) {
		
		log.debug("init TEC");
		ts = new HashMap<String, Task>();
		
		for(EventList el : list) {
			String p = el.getId();
			
			log.debug("participant = " + p);
			
			String g = StrategyHelper.getGroup(el);
			
			log.debug("group = " + g);
			
			Iterator<Event> it = el.iterator(); 
			NextEvent task;
			
			while((task = (NextEvent) StrategyHelper.getFollowingEventByType(it, "next")) != null) {
				
				log.debug("task section = " + task.getSection());
				
				if(task.getSection().startsWith("#task")) {
					Task t = new Task(task.getSection());
					
					NextWithResponsesEvent responses = (NextWithResponsesEvent) StrategyHelper.getFollowingEventByType(it, "next.response");
					
					log.debug("response section = " + responses.getSection());
					
					for(String q : responses.getResponsesKeys()) {
						log.debug("init: p=" + p + "; g=" + g + "; q=" + q + "; a=" + responses.getResponse(q));
						t.addAnswer(p, g, q, LikertScaleFivePoint.instance().scaleFloat(Integer.parseInt(responses.getResponse(q))));
					}
					ts.put(t.getTask(), t);
				}
				else
					log.debug("skipped");
			}
		}
	}
	
	@Override
	public Object[][] makeArrays() throws Exception {
		
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		for(Task t : ts.values()) 
			obj.addAll(t.make());
		
		return obj.toArray(new Object[0][0]);
	}

	
}
