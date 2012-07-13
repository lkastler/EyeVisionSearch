package eyevisionsearch.logic.helpers;

/**
 * creates multiple rows for for Strategy base classes.
 * @author lkastler
 *
 */
abstract public class MultipleRowContainer {
	
	/**
	 * returns an array of arrays of Objects from the corresponding rows in this MultipleRowsContainer.
	 * @return an array of arrays of Objects from the corresponding rows in this MultipleRowsContainer.
	 * @throws Exception thrown if array could not be created.
	 */
	abstract public Object[][] makeArrays() throws Exception;
}
