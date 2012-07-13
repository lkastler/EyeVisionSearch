package eyevisionsearch.logic.events;

/**
 * Event abstract class. Contains time and type of Event
 * 
 * @author lkastler
 *
 */
abstract public class Event {
	private long time;
	
	/**
	 * constructor
	 * @param time time stamp of this Event
	 */
	protected Event(long time) {
		this.time = time;
	}
	
	/**
	 * returns the type of this Event
	 * @return the type of this Event
	 */
	abstract public String getType();
	
	/**
	 * returns time stamp of this Event
	 * @return time stamp of this Event
	 */
	public long getTime() {
		return time;
	}
	
	abstract public String toString();
}
