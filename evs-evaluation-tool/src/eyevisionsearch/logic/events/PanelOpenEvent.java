package eyevisionsearch.logic.events;

public class PanelOpenEvent extends PanelEvent {

	PanelOpenEvent(long time) {
		super(time);
	}

	@Override
	public String getType() {
		return "panel.open";
	}

	@Override
	public String toString() {
		return "panelOpen[]";
	}

}
