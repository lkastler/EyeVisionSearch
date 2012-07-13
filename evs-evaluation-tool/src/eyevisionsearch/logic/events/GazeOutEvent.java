package eyevisionsearch.logic.events;

public class GazeOutEvent extends ItemEvent {

	public GazeOutEvent(long time, String uri) {
		super(time, uri);
	}

	@Override
	public String getType() {
		return "gaze.out";
	}

	@Override
	public String toString() {
		return "gazeout[]";
	}

}
