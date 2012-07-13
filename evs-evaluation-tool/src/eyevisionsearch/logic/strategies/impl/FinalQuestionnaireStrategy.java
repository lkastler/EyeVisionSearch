package eyevisionsearch.logic.strategies.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.db.Database;
import eyevisionsearch.logic.db.DatabaseException;
import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.events.NextWithResponsesEvent;
import eyevisionsearch.logic.helpers.LikertScaleFivePoint;
import eyevisionsearch.logic.helpers.StrategyHelper;
import eyevisionsearch.logic.strategies.Strategy;

public class FinalQuestionnaireStrategy extends Strategy {

	Database db;
	
	public FinalQuestionnaireStrategy() {
		try {
			db = Database.instance();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	// prepares Database
	private void prepare() throws ClassNotFoundException, SQLException {
		try {
			db.requestDatabaseWorkerExecution("ParticipantDatabaseWorker", list);
		
			try {
				db.execute("drop table finalquestionnaire");
			} catch(SQLException e){
				log.debug(e);
			}
			
			db.execute("create table finalquestionnaire(" +
				"p varchar(4) not null, " +
				"q int not null, " +
				"r int not null, " +
				"PRIMARY KEY (p,q))");
		
		} catch (DatabaseException e1) {
			log.error(e1);
		}
	}
	
	// evaluates EventLists 
	private void evaluate(ArrayList<EventList> list) {
		for(EventList el : list) {
			String p = el.getId();
			
			NextWithResponsesEvent ev;
			Iterator<Event> it = el.iterator();
			// find questionnaire
			while((ev = (NextWithResponsesEvent) StrategyHelper.getFollowingEventByType(it, "next.response"))!= null) {
				if(ev.getSection().equalsIgnoreCase("#qn3") || ev.getSection().equalsIgnoreCase("#qn5")) {
					log.debug("found final questionnaire");

					// parse it
					for(String q : ev.getResponsesKeys()) {
						
						Pattern pat = Pattern.compile("-?\\d+");
						Matcher m = pat.matcher(q);
						
						int qn = 0;
						while (m.find()) {
							
							//FIX IT: needed because A questions have a lower value than their equivalents in B and C 
							if(ev.getSection().equalsIgnoreCase("#qn3"))
								qn = Integer.parseInt(m.group()) - 5;
							else
								qn = Integer.parseInt(m.group()) - 9;
								
						}
						
						// put it into db
						try {
							
							log.debug("p=" + p + "; qn=" + qn + "; r=" + ev.getResponse(q));
							db.execute("insert into finalquestionnaire (p, q, r) values('" + p + "'," + qn + "," + ev.getResponse(q) + ")");
						} catch (SQLException e) {
							log.error(e);
						}
					}
				}
				else
					log.debug("skip: " + ev.getSection());
			}
		}
	}
	
	// create Object[][] for DefaultTableModel
	// layout: p, g, r_q1, r_q2 ....
	private Object[][] build() throws SQLException {
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		// get participants
		ResultSet p = db.executeQuery("select p, g from participant");
		
		while(p.next()) {
			ArrayList<Object> o = new ArrayList<Object>();
			o.add(p.getString("p"));
			o.add(p.getString("g"));
			
			// get questions & responses
			ResultSet r =  db.executeQuery("select q,r from finalquestionnaire where p='"+ p.getString("p") +"' order by q");
			
			while(r.next()) {
				log.debug(r.getInt("q"));
				o.add(LikertScaleFivePoint.instance().scaleFloat(r.getInt("r")));				
			}
			
			r.close();
			
			obj.add(o.toArray());
		}
		p.close();

		// sort for groups and participants 
		Collections.sort(obj, new Comparator<Object[]> (){
			@Override
			public int compare(Object[] o1, Object[] o2) {
				String s1 = (String)o1[1];
				String s2 = (String)o2[1];
				if(s1.equalsIgnoreCase(s2)) {
					return ((String)o1[0]).compareToIgnoreCase((String)o2[0]);
				}
				return s1.compareToIgnoreCase(s2);
			}
			
		});
		
		return obj.toArray(new Object[0][0]);
	}
	
	@Override
	public TableModel compile() throws ClassNotFoundException, SQLException {
		prepare();
		evaluate(list);
		
		ArrayList<Object> cols = new ArrayList<Object>();
		
		cols.add("P");
		cols.add("G");
		
		for(int i = 1; i <= 7; i++) {
			cols.add("Q " + i);
		}
		
		return new DefaultTableModel(build(), cols.toArray());
	}

	@Override
	public String getName() {
		return "Final Questionnaire Strategy";
	}

}
