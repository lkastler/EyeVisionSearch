package eyevisionsearch.logic.events;

public class ImageSaveEvent extends PanelEvent {

	private String uri;
	
	ImageSaveEvent(long time, String uri) {
		super(time);
		this.uri = uri;
	}

	
	@Override
	public String getType() {
		return "panel.imageSave";
	}

	@Override
	public String toString() {
		return "imageSave[]";
	}


	public String getURI() {
		return uri;
	}

}
