package eyevisionsearch.logic.strategies.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.db.Database;
import eyevisionsearch.logic.helpers.LikertScaleFivePoint;
import eyevisionsearch.logic.strategies.Strategy;

/**
 * provides a SPSS friendly variant to evaluate the task questionnaires.
 * @author lkastler
 *
 */
public class SPSSTaskEvaluationStrategy extends Strategy {

	Database db;
	
	/**
	 * default constructor, needed for Reflection
	 * @throws ClassNotFoundException database could not be created
	 * @throws SQLException database could not be created
	 */
	public SPSSTaskEvaluationStrategy() throws ClassNotFoundException, SQLException {
		super();
		db = Database.instance();
	}
	
	@Override
	public TableModel compile() throws Exception {
		
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		
		db.requestDatabaseWorkerExecution("ParticipantDatabaseWorker", list);
		db.requestDatabaseWorkerExecution("TaskEvaluationDatabaseWorker", list);

		// parses task numbers
		ResultSet t = db.executeQuery("select distinct t from taskeval");
		TreeSet<Integer> tnumbers = new TreeSet<Integer>();
		
		while(t.next()) {
			Pattern p = Pattern.compile("-?\\d+");
			Matcher m = p.matcher(t.getString("t"));
			
			while (m.find()) {
				tnumbers.add(Integer.parseInt(m.group()));
			}
		}
		
		t.close();

		// parses question numbers
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
		
		log.info("questions: " + Arrays.toString(qnumbers.toArray()));
		
		ResultSet p = db.executeQuery("select p, g from participant");
		
		// per participant
		while(p.next()) {
			ArrayList<Object> o = new ArrayList<Object>();
			o.add(p.getString("p"));
			o.add(p.getString("g"));
			
			// per task
			for(int tt : tnumbers) {
				o.add("T" + Integer.toString(tt));
				
				// per question
				for(int qt : qnumbers) {
					log.debug("select r from taskeval " +
							"where p='" + p.getString("p") + "' " + 
							"and t like '%" + tt + "' " +
							"and q like '%" + qt + "'");
					
					ResultSet r = db.executeQuery("select r from taskeval " +
							"where p='" + p.getString("p") + "' " + 
							"and t like '%" + tt + "' " +
							"and q like '%" + qt + "'");
					
					float response = -1;
					while(r.next()) {
						response = LikertScaleFivePoint.instance().scaleFloat(r.getInt("r"));
					}
					o.add(response);
				}
								
			}
			
			p.close();
			
			// sorts array by group and participant
			Collections.sort(obj, new Comparator<Object[]>() {

				@Override
				public int compare(Object[] o1, Object[] o2) {
					if(((String)o1[1]).equalsIgnoreCase((String)o2[1])) {
						return ((String)o1[0]).compareToIgnoreCase((String)o2[0]);
					}
					return ((String)o1[1]).compareToIgnoreCase((String)o2[1]);
				}
			
			});
			
			obj.add(o.toArray());
		}
		
		// makes columns
		ArrayList<String> cols = new ArrayList<String>();
		cols.add("P");
		cols.add("G");
		for(int i = 0; i < 6; i++) { 
			cols.add("Task");
			for(int j = 0; j < 10; ++j) {
				cols.add("Q" + (j+1));
			}
		}
		
		return new DefaultTableModel(obj.toArray(new Object[0][0]), cols.toArray());
	}

	@Override
	public String getName() {
		return "SPP Task Evaluation";
	}

}
