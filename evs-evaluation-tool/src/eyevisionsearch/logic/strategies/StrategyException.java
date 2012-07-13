package eyevisionsearch.logic.strategies;

/**
 * Exception for <code>Strategy</code> based exception. 
 * @author lkastler
 *
 */
public class StrategyException extends Exception {

	/**
	 * constructor
	 * @param msg failure message
	 */
	public StrategyException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;

}
