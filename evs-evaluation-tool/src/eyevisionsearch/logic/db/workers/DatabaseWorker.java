package eyevisionsearch.logic.db.workers;

import java.util.List;

import eyevisionsearch.logic.db.DatabaseException;
import eyevisionsearch.logic.events.EventList;

/**
 * Worker class for creating database structures of widely used concepts for strategies.
 * @author lkastler
 *
 */
public abstract class DatabaseWorker {

	/**
	 * creates database structures from the specified List of Eventlists
	 * @param list List of EventLists for the creation of requested database structures, modeled by this DatabaseWorker
	 * @throws DatabaseException thrown if desired database structures could not be created.
	 */
	abstract public void execute(List<EventList> list) throws DatabaseException;
}
