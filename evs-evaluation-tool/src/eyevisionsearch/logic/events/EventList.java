package eyevisionsearch.logic.events;

import java.util.ArrayList;

/**
 * ArrayList<Event> wrapper
 * @author lkastler
 *
 */
public class EventList extends ArrayList<Event> {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * constructor with specified id for this EventList
	 * @param id
	 */
	public EventList(String id) {
		super();
		this.id = id;
	}
	
	/**
	 * returns id of this EventList
	 * @return id of this EventList
	 */
	public String getId() {
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuffer s = new StringBuffer("eventlist[");
	
		for(Event cur : this) {
			
			s.append(cur.toString());
		}
		
		s.append("]");
		return s.toString();
	}
	
}
