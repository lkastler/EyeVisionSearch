package eyevisionsearch.logic.db;

import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.Test;

/**
 * test for the Database classes.
 * 
 * @author lkastler
 *
 */
public class DatabaseTest {

	public DatabaseTest(){}
	
	@Test
	public void testInitiation() throws Exception {
		Database db = Database.instance();
		
		db.execute("create table test(A int not null, B int not null)");
		db.execute("insert into test values(1,1)");
		
		ResultSet set = db.executeQuery("select * from test");
		
		assertEquals(set.getMetaData().getColumnCount(), 2);
		
		int count = 0;
		
		while(set.next()) {
			count += set.getInt("a");
		}
		
		set.close();
		
		assertEquals(count, 1);
		
		db.execute("drop table test");
		
		db.close();
	}
}
