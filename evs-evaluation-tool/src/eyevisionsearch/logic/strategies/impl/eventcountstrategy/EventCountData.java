package eyevisionsearch.logic.strategies.impl.eventcountstrategy;

import java.util.ArrayList;

/**
 * stores data for EventCount Strategies.
 * @author lkastler
 *
 */
public class EventCountData {
	private int mouse;
	private int gaze;
	private int url;
	private int save;
	private int panel;

	public EventCountData() {
		mouse = 0;
		gaze = 0;
		url = 0;
		save = 0;
		panel = 0;
	}

	public int getMouseEventsOccurs() {
		return mouse;
	}

	public void addMouseEventOccur() {
		++mouse;
	}

	public int getGazeEventOccurs() {
		return gaze;
	}

	public void addGazeEventOccur() {
		++gaze;
	}

	public int getUrlEventOccurs() {
		return url;
	}

	public void addUrlEventOccur() {
		++url;
	}

	public int getImageSaveEventOccurs() {
		return save;
	}

	public void addImageSaveEventOccur() {
		++save;
	}

	public int getPanelOpenEventOccurs() {
		return panel;
	}

	public void addPanelOpenEventOccur() {
		++panel;
	}

	public void disableSpecialEvents() {
		panel = -1;
		save = -1;
	}
	
	public Object[] toArray() {
		ArrayList<Integer> o = new ArrayList<Integer>();
		
		o.add(mouse);
		o.add(gaze);
		o.add(url);
		o.add(save);
		o.add(panel);
		
		return o.toArray();
	}
}