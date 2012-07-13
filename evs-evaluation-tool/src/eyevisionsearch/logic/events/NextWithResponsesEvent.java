package eyevisionsearch.logic.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NextWithResponsesEvent extends NextEvent {

	private HashMap<String, String> responses;
	
	protected NextWithResponsesEvent(long time, String section, Map<String, String> response) {
		super(time, section);
		this.responses = new HashMap<String, String>(response);
	}
	
	public Set<String> getResponsesKeys() {
		return responses.keySet();
	}
	
	public Map<String, String> getResponses() {
		return responses;
	}

	public String getResponse(String key) {
		return responses.get(key);
	}

	@Override
	public String getType() {
		return "next.response";
	}
	
	public String toString() {
		return super.toString();
	}
}
