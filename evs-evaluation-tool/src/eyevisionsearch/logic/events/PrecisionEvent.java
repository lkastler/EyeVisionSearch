package eyevisionsearch.logic.events;

import java.util.ArrayList;
import java.util.List;

public class PrecisionEvent extends Event {

	private ArrayList<String> truePositives;
	
	PrecisionEvent(long time, List<String> tp) {
		super(time);
		truePositives = new ArrayList<String>(tp);
	}
	
	public List<String> getTruePositives() {
		return truePositives;
	}
	
	@Override
	public String getType() {
		return "precision";
	}

	@Override
	public String toString() {
		
		return "precision[t=" + getTime() + ";#tp=" + truePositives.size() + "]";
	}

}
