package eyevisionsearch.logic.events;

public class PanelHideEvent extends PanelEvent {

	PanelHideEvent(long time) {
		super(time);
	}

	@Override
	public String getType() { 		
		return "panel.hide";
	}

	@Override
	public String toString() {
		return "PanelHide[]";
	}

}
