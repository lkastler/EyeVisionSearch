package eyevisionsearch.logic.events;

/**
 * Exception thrown by EventFactory
 * 
 * @author lkastler
 *
 */
public class EventFactoryException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * creates Exception with specified message and cause
	 * @param message message for surviving dependant
	 * @param cause cause that throws this Exception 
	 */
	public EventFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * creates Exception with specified message
	 * @param message message for surviving dependant
	 */
	public EventFactoryException(String message) {
		super(message);
	}
}
