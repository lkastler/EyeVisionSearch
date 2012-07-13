package eyevisionsearch.logic.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class PanelBackupEvent extends PanelEvent {

	private HashMap<String, Double> ranking;
	
	public PanelBackupEvent(long time) {
		super(time);
		ranking = new HashMap<String, Double>();
	}
	
	PanelBackupEvent(long time, JSONObject d) throws JSONException {
		this(time);
		
		if(d.length() > 0) {
			String[] names = JSONObject.getNames(d);
			if(names.length > 0) {
				for(String name : names) {
						ranking.put(name, d.getJSONObject(name).getDouble("ranking"));
				}
			}
		}
	}

	public int getSize() {
		return ranking.size();
	}
	
	public Set<String> getRankingKeys() {
		return ranking.keySet();
	}
	
	public double getRanking(String key) {
		return ranking.get(key);
	}
	
	public Map<String, Double> getRankings() {
		return ranking;
	}
	
	@Override
	public String getType() {
		return "panel.backup";
	}

	@Override
	public String toString() {
		return "PanelBackup[]";
	}

}
