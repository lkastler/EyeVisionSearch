package eyevisionsearch.logic.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.NextWithResponsesEvent;

public class TaskResponse {

	private String task;
	private HashMap<String, Integer> response;

	public static TaskResponse makeTaskResponse(Iterator<Event> it) {
		String task = null;
		NextEvent taskEvent;
		while(it.hasNext()) {
			taskEvent = (NextEvent)StrategyHelper.getFollowingEventByType(it, "next");
			
			if(taskEvent == null)
				return null;
			
			if(taskEvent.getSection().startsWith("#task")) {
				task = taskEvent.getSection();
				break;
			}
		}
		
		if(task == null)
			return null;
				
		NextWithResponsesEvent nwr =  (NextWithResponsesEvent)StrategyHelper.getFollowingEventByType(it, "next.response");
		
		if(nwr == null || !nwr.getSection().startsWith("#qn"))
			return null;
		
		
		HashMap<String, Integer> response = new HashMap<String, Integer>();
		
		for(String key : nwr.getResponsesKeys()) {
			response.put(key, Integer.parseInt(nwr.getResponse(key)));
		}
		
		return new TaskResponse(task, response);
	}
	
	private TaskResponse(String task, HashMap<String, Integer> response) {
		this.task = task;
		this.response = response;
	}
	
	public String getTask() {
		return task;
	}
	
	public Map<String, Integer> getResponse() {
		return response;
	}
}
