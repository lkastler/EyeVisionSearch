package eyevisionsearch.logic.strategies.impl.taskevaluationstrategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

class Task {
	
	static Logger log = Logger.getLogger(Task.class);
	
	private String task;
	private HashMap<String, Question> qs;
	
	Task(String task) {
		this.task = task;
		qs = new HashMap<String, Question>();
		
		log.debug("task created");
	}
	
	void addAnswer(String participant, String group, String question, float f) {
		log.debug(getTask() + ": p=" + participant + "; g=" + group + "; q=" + question + "; a=" + f);
		
		if(!qs.containsKey(question))
			qs.put(question, new Question(question));
		
		qs.get(question).addAnswer(participant, group, f);
	}
	
	ArrayList<Object[]> make() {
		log.debug("make task: " + getTask());
	
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		String[] t = new String[2];
		t[0] = "task";
		t[1] = getTask();
		
		obj.add(t);
		
		for(Question q : qs.values())
			obj.addAll(q.make());
		
		return obj;
	}

	public String getTask() {
		return task;
	}
}
