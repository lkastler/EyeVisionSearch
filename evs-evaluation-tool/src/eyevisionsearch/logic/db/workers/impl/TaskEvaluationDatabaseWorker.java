package eyevisionsearch.logic.db.workers.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.db.Database;
import eyevisionsearch.logic.db.DatabaseException;
import eyevisionsearch.logic.db.workers.DatabaseWorker;
import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.NextWithResponsesEvent;
import eyevisionsearch.logic.helpers.StrategyHelper;

public class TaskEvaluationDatabaseWorker extends DatabaseWorker {

	private static Logger log = Logger.getLogger(TaskEvaluationDatabaseWorker.class);
	
	@Override
	public void execute(List<EventList> list) throws DatabaseException {
		try {
			Database db = Database.instance();
		
		try {
			db.execute("drop table taskeval");
		}catch(SQLException e) {
			log.debug(e);
		}
			
		db.requestDatabaseWorkerExecution("ParticipantDatabaseWorker", list);
		
		db.execute("create table taskeval(" +
				"p varchar(4) not null, " +
				"t varchar(10) not null, " +
				"q varchar(5) not null, " +
				"r int not null, " +
				"PRIMARY KEY (p,t,q))"
			);
		
		try {	
			for(EventList el : list) {
				String p = el.getId();
			
				log.debug("participant = " + p);
				
				Iterator<Event> it = el.iterator(); 
				NextEvent task = null;
					
				while((task = (NextEvent) StrategyHelper.getFollowingEventByType(it, "next")) != null) {
						
					log.debug("task section = " + task.getSection());
						
					if(task.getSection().startsWith("#task")) {
							
						NextWithResponsesEvent responses = (NextWithResponsesEvent) StrategyHelper.getFollowingEventByType(it, "next.response");
							
						log.debug("response section = " + responses.getSection());
							
						for(String q : responses.getResponsesKeys()) {
							try {
								log.debug("add: p=" + p + "; t=" + task.getSection() + "; q=" + q + "; r=" + responses.getResponse(q));
									
								db.execute("insert into taskeval (p,t,q,r) " +
										"values('" 
										+ p + "','"
										+ task.getSection() + "','" 
										+ q + "'," 
										+ responses.getResponse(q) + ")"
									);
									
							} catch (SQLException e) {
								log.warn(e);
							}
						}
					}
					else {
						log.debug("skipped");
					}
				}
			}
		} catch(Exception e) {
			log.error(e);
		}
	} catch(Exception e) {
		throw new DatabaseException(e);
	}		
	}	
}
