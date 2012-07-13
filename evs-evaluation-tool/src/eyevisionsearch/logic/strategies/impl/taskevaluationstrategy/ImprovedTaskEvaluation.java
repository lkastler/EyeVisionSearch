package eyevisionsearch.logic.strategies.impl.taskevaluationstrategy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.db.Database;
import eyevisionsearch.logic.db.DatabaseException;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.helpers.LikertScaleFivePoint;

/**
 * improved variant for the evaluation of task questionnaires.
 * @author lkastler
 *
 */
public class ImprovedTaskEvaluation {

	static Logger log = Logger.getLogger(ImprovedTaskEvaluation.class);
	
	private Database db;
	
	/**
	 * default constructor for reflection.
	 * @throws ClassNotFoundException thrown if Database could not be created.
	 * @throws SQLException thrown if Database could not be created.
	 */
	public ImprovedTaskEvaluation() throws ClassNotFoundException, SQLException {
		log.debug("creating ImprovedTaskEvaluation");
		
		db = Database.instance();
		
		log.debug("done");
	}
	
	/**
	 * parses given ArrayList of EventLists into the Database.
	 * @param list ArrayList of EventLists to parse
	 * @throws DatabaseException thrown if give ArrayList of EventLists could not be added to Database.
	 */
	public void parseEvents(List<EventList> list) throws DatabaseException {
		db.requestDatabaseWorkerExecution("TaskEvaluationDatabaseWorker", list);
	}
	
	/**
	 * creates an Object[][] from data
	 * @return an Object[][] created from data
	 */
	public Object[][] getData() {
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		try {
			
			// get questions
			ResultSet q = db.executeQuery("select distinct q from taskeval");
			
			TreeSet<Integer> qnumbers = new TreeSet<Integer>();
			
			while(q.next()) {
				Pattern p = Pattern.compile("-?\\d+");
				Matcher m = p.matcher(q.getString("q"));
				
				while (m.find()) {
					qnumbers.add(Integer.parseInt(m.group()));
				}
			}
			q.close();
			
			// get groups
			ResultSet g = db.executeQuery("select distinct g from participant");

			ArrayList<String> groups = new ArrayList<String> ();
			
			while(g.next()){
				groups.add(g.getString("g"));
			}
			
			g.close();
			
			// get tasks
			ResultSet t = db.executeQuery("select distinct t from taskeval order by t");
			
			while(t.next()) {
				
				obj.add(new String[] {"TASK", t.getString("t")});
				
				for(int qn : qnumbers) {
					obj.add(new String[] {"QUESTION", Integer.toString(qn)});
					
					for(String gr : groups) {
						ResultSet r = db.executeQuery("select t.r from participant p, taskeval t " +
								"where t.p = p.p " +
								"and p.g = '" + gr + "' " +
								"and t.q like '%" + qn + "' " +
								"and t.t = '" + t.getString("t") + "'");
						
						ArrayList<Object> row = new ArrayList<Object>();
						
						row.add(gr);
						
						while(r.next()) {
							row.add(LikertScaleFivePoint.instance().scaleFloat(r.getInt("r")));
						}
						
						r.close();
						
						obj.add(row.toArray());
						
					}
				}
			}
			
			t.close();
		} catch (SQLException e) {
			log.error(e);
		}
			
		return obj.toArray(new Object[0][0]);
	}
}
