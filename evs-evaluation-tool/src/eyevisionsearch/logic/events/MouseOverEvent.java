package eyevisionsearch.logic.events;

public class MouseOverEvent extends ItemEvent {

	protected MouseOverEvent(long time, String uri) {
		super(time, uri);
	}

	@Override
	public String getType() {
		return "mouse.over";
	}

	@Override
	public String toString() {
		return "mouseover";
	}
	
	

}
