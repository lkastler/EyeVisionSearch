package eyevisionsearch.logic.strategies;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.events.EventList;

/**
 * abstract strategy to create from a list of Events an OpenDocument sheet. 
 * @author lkastler
 *
 */
abstract public class Strategy {
	
	protected Logger log = Logger.getLogger(Strategy.class);
	
	protected ArrayList<EventList> list;
	/**
	 * constructor
	 */
	protected Strategy() {
		list = new ArrayList<EventList>();
	}
	
	/**
	 * sets the list of EventLists to specified list
	 * @param list list of EventLists
	 */
	public void setList(List<EventList> list) {
		log.debug("list set: " + list.toString());
		this.list = new ArrayList<EventList>(list);
	}

	// TODO
	abstract public TableModel compile() throws Exception;
	
	/**
	 * returns name of this Strategy
	 * @return name of this Strategy
	 */
	abstract public String getName();
}
