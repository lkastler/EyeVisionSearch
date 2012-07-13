package eyevisionsearch.logic.events;

public class TopEvent extends ItemEvent {

	private final int position;

	public TopEvent(long time, String uri, int position) {
		super(time,uri);
		this.position = position;
		
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String getType() {
		return "top";
	}

	@Override
	public String toString() {
		return "top[]";
	}

}
