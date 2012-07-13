package eyevisionsearch.logic.strategies.impl.taskevaluationstrategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

class Question {
	
	static Logger log = Logger.getLogger(Question.class);
	
	private String question;
	private HashMap<String, Group> gs;
	
	Question(String question) {

		this.question = question;
		gs = new HashMap<String, Group>();
		
		log.debug("question created");
	}
	
	void addAnswer(String participant, String group, float f) {
		log.debug(question + ": p=" + participant + "; g=" + group + "; a=" + f);
		
		if(!gs.containsKey(group))
			gs.put(group, new Group(group));
		
		gs.get(group).addAnswer(participant, f);
	}
	
	ArrayList<Object[]> make() {
		log.debug("make question: " + question);
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		String[] q = new String[2];
		q[0] = "question";
		q[1] = question;
		obj.add(q);
		
		for(Group g : gs.values())
			obj.addAll(g.make());
		
		return obj;
	}
}
