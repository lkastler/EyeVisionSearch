package eyevisionsearch.logic.helpers;

/**
 * abstract class for all classes that creates a single row for Strategy based classes.
 * @author lkastler
 *
 */
abstract public class RowContainer {

	/**
	 * creates an Object array that corresponds with the single row of this RowContainer.
	 * @return an Object array that corresponds with the single row of this RowContainer.
	 * @throws Exception thrown if Object array could not be created.
	 */
	abstract public Object[] rowToArray() throws Exception;
}
