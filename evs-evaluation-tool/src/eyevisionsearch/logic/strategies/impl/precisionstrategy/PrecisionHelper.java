package eyevisionsearch.logic.strategies.impl.precisionstrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PrecisionHelper {

	private HashMap<String, Double> positives;
	private ArrayList<String> truePositives;
	
	public PrecisionHelper(Map<String, Double> positives) {
		this.positives = new HashMap<String, Double>(positives);
		truePositives = null;
	}
	
	public void addTruePosivitves(List<String> tp) {
		truePositives = new ArrayList<String>(tp);
	}
	
	public int getHitsWithin() {
		return getHitsWithin(0);
	}
	
	public int getHitsWithin(int range) {
		// sort with ranking
		ArrayList<Entry<String,Double>> sorter = new ArrayList<Entry<String,Double>>(positives.entrySet());
		
		Collections.sort(sorter, new Comparator<Entry<String,Double>>() {

			@Override
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		
		});
		int bound = sorter.size();
		
		if(range > 0 && range < sorter.size())
			bound = range;
			
		
		ArrayList<String> candidates = new ArrayList<String>();
		
		for(int i = 0; i < bound; ++i) {
			candidates.add(sorter.get(i).getKey());
		}
		
		// now compare
		
		int result = 0;
		
		for(String tp: truePositives) {
			for(String c : candidates) {
				if(tp.equalsIgnoreCase(c))
					result++;
			}
		}
		
		
		return result;
	}
	
	public int getPositivesSize() {
		return positives.size();
	}
	
	public int getTruePositivesSize() {
		return truePositives.size();
	}
	
}