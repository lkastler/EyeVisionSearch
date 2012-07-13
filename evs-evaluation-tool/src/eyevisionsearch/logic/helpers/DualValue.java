package eyevisionsearch.logic.helpers;

/**
 * structure to save two associated values together.
 * @author lkastler
 *
 * @param <T1>
 * @param <T2>
 */
public class DualValue<T1, T2> {

	private T1 first;
	private T2 last;
	
	/**
	 * constructor
	 * @param first the first value.
	 * @param last  the last value.
	 */
	public DualValue(T1 first, T2 last) {
		super();
		this.first = first;
		this.last = last;
	}

	/**
	 * returns the first value.
	 * @return the first value.
	 */
	public T1 getFirst() {
		return first;
	}

	/**
	 * returns the last value.
	 * @return the last value.
	 */
	public T2 getLast() {
		return last;
	}
	
	
}
