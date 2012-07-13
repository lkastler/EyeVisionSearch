package eyevisionsearch.logic.strategies.impl.taskevaluationstrategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

class Group {
	
	static Logger log = Logger.getLogger(Group.class);
	
	private String group;
	private HashMap<String, Float> ps;
	
	Group(String group) {
		this.group = group;
		ps = new HashMap<String, Float>();
		
		log.debug("group created");
	}
	
	void addAnswer(String participant, float f) {
		log.debug(group + ": p=" + participant + "; a=" + f);
		
		ps.put(participant, f) ;
	}
	
	ArrayList<Object[]> make() {
		log.debug("make group: " + group);
		ArrayList<Object[]> a = new ArrayList<Object[]>(3);
		
		String[] g = new String[2];
		g[0] = "group:";
		g[1] = group;
		a.add(g);
		
		log.debug("ps.keySet.size= " + ps.keySet().size());
		log.debug("ps.values.size= " + ps.values().size());
		
		a.add(ps.keySet().toArray(new Object[0]));
		a.add(ps.values().toArray(new Object[0]));
		
		return a;
	}
}
