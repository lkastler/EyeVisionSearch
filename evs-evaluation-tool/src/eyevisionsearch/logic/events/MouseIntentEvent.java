package eyevisionsearch.logic.events;

public class MouseIntentEvent extends ItemEvent {

	protected MouseIntentEvent(long time, String uri) {
		super(time, uri);
	}

	@Override
	public String getType() {
		return "mouse.intent";
	}

	@Override
	public String toString() {
		
		return "mouseIntent[]";
	}

}
