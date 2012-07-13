package eyevisionsearch.logic.events;

public class EndEvent extends Event {

	protected EndEvent(long time) {
		super(time);
	}

	@Override
	public String getType() {
		return "end";
	}

	@Override
	public String toString() {
		return "end[t=" + getTime() + "]";
	}

}
