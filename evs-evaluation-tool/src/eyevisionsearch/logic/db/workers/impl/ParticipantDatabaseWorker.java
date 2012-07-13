package eyevisionsearch.logic.db.workers.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.db.Database;
import eyevisionsearch.logic.db.DatabaseException;
import eyevisionsearch.logic.db.workers.DatabaseWorker;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.helpers.StrategyHelper;

/**
 * creates a Participant Table.
 * Table Name: participant
 * Content:
 * - p: name of participant : varchar(4) not null PRIMARY KEY
 * - g: group of participant : varchar(1) not null)
 * 
 * @author lkastler
 *
 */
public class ParticipantDatabaseWorker extends DatabaseWorker {

	Logger log = Logger.getLogger(ParticipantDatabaseWorker.class);
	
	public ParticipantDatabaseWorker(){
		log.debug("ParticipantDatabaseWorker created");
	}
	
	@Override
	public void execute(List<EventList> list) throws DatabaseException {
		
		try {
			Database  db = Database.instance();
			
			try {
				db.execute("drop table participant");
			} catch(SQLException e) {
				log.debug("don't care");
			}
			
			db.execute("create table participant(p varchar(4) not null PRIMARY KEY, g varchar(1) not null)");
			
			for(EventList el : list) {
				String p = el.getId();
	
				log.debug("participant = " + p);
	
				String g = StrategyHelper.getGroup(el);
	
				log.debug("group = " + g);			
	 
				db.execute("insert into participant (p,g) values('" + p + "','" + g + "')");
			}
		
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}
}
