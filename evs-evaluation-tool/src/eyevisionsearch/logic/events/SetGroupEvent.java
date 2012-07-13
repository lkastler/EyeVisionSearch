package eyevisionsearch.logic.events;

public class SetGroupEvent extends Event {

	private String group;
	
	protected SetGroupEvent(long time, String group) {
		super(time);
		this.group = group;
	}

	@Override
	public String getType() {
		return "setgroup";
	}

	public String getGroup() {
		return group;
	}
	
	@Override
	public String toString() {
		return "SetGroupEvent[t=" + getTime() + ";group=" + group + "]";
	}

}
