package eyevisionsearch.logic.events;

public class GazeOverEvent extends ItemEvent {

	public GazeOverEvent(long time, String uri) {
		super(time,uri);
	}

	@Override
	public String getType() {
		return "gaze.over";
	}

	@Override
	public String toString() {
		return "gazeover[]";
	}
}
