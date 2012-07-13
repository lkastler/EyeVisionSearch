package eyevisionsearch.logic.strategies.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.impl.precisionstrategy.PrecisionContainer;

public class PrecisionStrategy extends Strategy {

	public PrecisionStrategy() {
		super();
	}

	@Override
	public TableModel compile() throws Exception {
		ArrayList<PrecisionContainer> cont = new ArrayList<PrecisionContainer>();
		for(EventList el : list) {
			cont.add(new PrecisionContainer(el));
		}
		
		Collections.sort(cont, new Comparator<PrecisionContainer>() {

			@Override
			public int compare(PrecisionContainer o1, PrecisionContainer o2) {
				if(o1.getGroup().equals(o2.getGroup()))
					return o1.getId().compareToIgnoreCase(o2.getId());
				return o1.getGroup().compareToIgnoreCase(o2.getGroup());
			}
			
		});
		
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		for(PrecisionContainer c : cont) {
			if(!c.getGroup().equalsIgnoreCase("A"))
				obj.add(c.rowToArray());
		}
		
		
		ArrayList<Object> cols = new ArrayList<Object>();
		
		cols.add("P");
		cols.add("G");
		for(int i = 0; i < 6; i++) {
			cols.add("Task");
			cols.add("P");
			cols.add("TP");
			cols.add("hits");
			cols.add("hits 10");
			cols.add("hits 20");
		}
		
		
		
		return new DefaultTableModel(obj.toArray(new Object[0][0]), cols.toArray());
	}

	@Override
	public String getName() {
		return "Precision Strategy";
	}

}
