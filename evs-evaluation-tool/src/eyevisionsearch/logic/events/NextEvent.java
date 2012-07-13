package eyevisionsearch.logic.events;

public class NextEvent extends Event {

	private String section;
	
	protected NextEvent(long time, String section) {
		super(time);
		this.section = section;
	}

	@Override
	public String getType() {
		return "next";
	}

	/**
	 * returns the section of this NextEvent
	 * @return the section of this NextEvent
	 */
	public String getSection() {
		return section;
	}

	@Override
	public String toString() {
		return "next[t=" + getTime()+ "]";
	}
	
}
