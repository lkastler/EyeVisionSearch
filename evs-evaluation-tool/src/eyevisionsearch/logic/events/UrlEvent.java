package eyevisionsearch.logic.events;

public class UrlEvent extends ItemEvent {

	public UrlEvent(long time, String url) {
		super(time, url);
	}

	@Override
	public String getType() {
		return "url";
	}

	@Override
	public String toString() {
		return "Url[]";
	}

}
