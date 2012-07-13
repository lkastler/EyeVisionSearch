package eyevisionsearch.logic.events;

public class MouseOutEvent extends ItemEvent {

	protected MouseOutEvent(long time, String uri) {
		super(time, uri);
	}

	@Override
	public String getType() {
		return "mouse.out";
	}

	@Override
	public String toString() {
		return "mouseout[]";
	}

}
