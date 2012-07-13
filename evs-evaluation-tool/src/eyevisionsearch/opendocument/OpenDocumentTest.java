package eyevisionsearch.opendocument;

import java.io.File;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * test for the OpenDocument class.
 * @author lkastler
 *
 */
public class OpenDocumentTest {

	public OpenDocumentTest() {}
	
	@org.junit.Test
	public void test_File_Creation() throws Exception {			
		OpenDocument sh = new OpenDocument();
		Object[][] data = new Object[3][1];
		data[0] = new Object[]{"A"};
		data[1] = new Object[]{"A"};
		data[2] = new Object[]{"A"};
		
		Object[] head = {"A"};
		
		TableModel t = new DefaultTableModel(data, head);
		
		sh.addSheet("test", t);
		
		sh.saveAs(new File("test.ods"));
	}
}
