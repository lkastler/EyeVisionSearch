package eyevisionsearch.logic.events;

public class ForcedNextEvent extends Event {

	protected ForcedNextEvent(long time) {
		super(time);
	}

	@Override
	public String getType() {
		return "forced";
	}

	@Override
	public String toString() {
		return "ForcedNext[" + getTime() +"]";
	}
	

}
