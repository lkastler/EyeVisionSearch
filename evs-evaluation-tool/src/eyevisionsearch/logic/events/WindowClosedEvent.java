package eyevisionsearch.logic.events;

public class WindowClosedEvent extends Event {

	public WindowClosedEvent(long time) {
		super(time);
	}

	@Override
	public String getType() {
		return "window.close";
	}

	@Override
	public String toString() {
		return "WindowClosed[]";
	}

}
