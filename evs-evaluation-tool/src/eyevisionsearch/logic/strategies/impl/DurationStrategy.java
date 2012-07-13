package eyevisionsearch.logic.strategies.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.impl.durationstrategy.DurationContainer;

/**
 * measures the duration of an eventlist from the 1st next evnet to the end event
 * @author lkastler
 *
 */
public class DurationStrategy extends Strategy {
	
	/**
	 * constructor
	 * @throws IOException
	 */
	public DurationStrategy() {
		super();
	}
	
	@Override
	public TableModel compile() throws Exception {
		log.debug("list size = " + list.size());

		ArrayList<DurationContainer> buf = new ArrayList<DurationContainer>();
		for(EventList el : list)
			buf.add(new DurationContainer(el));
		
		Collections.sort(buf, new Comparator<DurationContainer>() {

			@Override
			public int compare(DurationContainer o1, DurationContainer o2) {
				if(!o1.getGroup().equalsIgnoreCase(o2.getGroup()))
					return o1.getGroup().compareToIgnoreCase(o2.getGroup());
				else
					return o1.getId().compareToIgnoreCase(o2.getId());
			}
			
		});
		
		ArrayList<Object[]> out = new ArrayList<Object[]>(buf.size());
		for(DurationContainer c : buf)
			out.add(c.rowToArray());
		
		Object[] col = {"participant", "group", "T1[ms]", "T2[ms]", "T3[ms]", "T4[ms]", "T5[ms]", "T6[ms]"};
		return new DefaultTableModel(out.toArray(new Object[0][0]), col);
	}

	@Override
	public String getName() {
		return "Duration Strategy";
	}
}
