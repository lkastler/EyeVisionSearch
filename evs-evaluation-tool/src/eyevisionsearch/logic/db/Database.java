package eyevisionsearch.logic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eyevisionsearch.logic.db.workers.DatabaseWorker;
import eyevisionsearch.logic.events.EventList;
import eyevisionsearch.logic.strategies.Strategy;

/**
 * handles database access for strategies.
 * 
 * @author lkastler
 *
 */
public class Database {

	static Logger log = Logger.getLogger(Database.class);
	
	private static Database instance;
	
	private Connection con;
	private String db_position = "test"; 
	
	private ArrayList<String> workers;

	private static String WORKERS_IMPL = DatabaseWorker.class.getPackage().getName() + ".impl";
	
	/**
	 * creates database 
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Database() throws ClassNotFoundException, SQLException {
		workers = new ArrayList<String>();
		
		log.info("creating database " + db_position);
		
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");        
        
        con = DriverManager.getConnection("jdbc:derby:" + db_position + ";create=true");
        
        log.info("done");
	}
	
	/**
	 * executes given SQL statement on this database.
	 * @param sql SQL statement to execute on this Database.
	 * @return returns true if SQL statement was successfully executed
	 * @throws SQLException thrown if statement could not be executed
	 */
	public boolean execute(String sql) throws SQLException {
		return con.createStatement().execute(sql);
	}
	
	/**
	 * executes the given SQL query on the database and returns a ResultSet with the answer.
	 * @param sql SQL query for the execution on the database.
	 * @return ResultSet with the response for the specified SQL query.
	 * @throws SQLException thrown if query could not be executed.
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return con.createStatement().executeQuery(sql);
	}
	
	/**
	 * closes database connection.
	 * @throws SQLException thrown if connection could not be closed.
	 */
	public void close() throws SQLException {
		con.close();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		close();
	}
	
	/**
	 * executes DatabaseWorker, specified by name, with specified List of EventLists on the Database. 
	 * If the DatabaseWorker was already executed, the method will proceed.
	 * @param name name of requested DatabaseWorker.
	 * @param list List of EventLists for the DatabaseWorker execution.
	 * @throws DatabaseException thrown if DatabaseWorker could not be executed. 
	 */
	public void requestDatabaseWorkerExecution(String name, List<EventList> list) throws DatabaseException {
		log.debug("try to execute: " + name);
		synchronized(workers) {
			try {
				if(!workers.contains(name)) {
					
					ClassLoader loader = Strategy.class.getClassLoader();
					@SuppressWarnings("unchecked")
					DatabaseWorker a = ((Class<DatabaseWorker>) loader.loadClass(WORKERS_IMPL  + "." + name)).newInstance();
					
					log.debug("execute: " + a.getClass().getName());
					a.execute(list);
					
					workers.add(name);
				}
				else
					log.debug(name + " already executed");
			} catch(Exception e) {
				throw new DatabaseException(e);
			}
		}
	}

	/**
	 * returns the instance of this Database.
	 * @return Singleton-Object of this Database.
	 * @throws ClassNotFoundException thrown if JDBC class could not be found.
	 * @throws SQLException thrown if thrown if connection to the database could not be initialized.
	 */
	public static Database instance() throws ClassNotFoundException, SQLException {
		if(instance == null)
			instance = new Database();
		return instance;
	}
}
