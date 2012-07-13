package eyevisionsearch.logic.events;

public class StartEvent extends Event {

	StartEvent(long time) {
		super(time);
	}

	@Override
	public String getType() {
		return "start";
	}

	@Override
	public String toString() {
		return "start[t=" +getTime() + "]";
	}

	
}
