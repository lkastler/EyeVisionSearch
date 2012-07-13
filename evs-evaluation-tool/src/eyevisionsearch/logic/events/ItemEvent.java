package eyevisionsearch.logic.events;

abstract public class ItemEvent extends Event {

	private final String uri;

	protected ItemEvent(long time, String uri) {
		super(time);
		this.uri = uri;
	}

	public String getURI() {
		return uri;
	}

}
