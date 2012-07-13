package eyevisionsearch.opendocument;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.table.TableModel;

import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 * OpenDocument handler.
 * @author lkastler
 *
 */
public class OpenDocument {

	
	private SpreadSheet spreadSheet;
	
	/**
	 * constructor, creates empty OpenDocument with 'lib/Template.ods' as template
	 * @throws IOException
	 */
	public OpenDocument() throws IOException {
		spreadSheet = SpreadSheet.create(new ODPackage(new File("lib/Template.ods")));
	}
	
	/**
	 * adds specified TableModel as sheet with specified name
	 * @param name name of sheet
	 * @param model model of sheet
	 * @throws IOException thrown if something went wrong
	 */
	public void addSheet(String name, TableModel model) throws IOException {
		
		SpreadSheet buffer = SpreadSheet.createEmpty(model);
		buffer.getSheet(0).setName(name);
		
		spreadSheet.getSheet("default").getElement().getParentElement()
			.addContent(spreadSheet.getSheetCount(), 
				(org.jdom.Element)buffer.getSheet(0).getElement().clone()
			);
	}
	
	/**
	 * saves OpenDocument to specified file
	 * @param file file to save OpenDocument
	 * @throws FileNotFoundException thrown if file could not be found
	 * @throws IOException thown if something went wrong
	 */
	public void saveAs(File file) throws FileNotFoundException, IOException {
		spreadSheet.saveAs(file);
	}
}

