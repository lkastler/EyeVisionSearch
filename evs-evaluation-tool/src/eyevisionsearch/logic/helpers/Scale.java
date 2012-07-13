package eyevisionsearch.logic.helpers;

/**
 * provides methods to use different scalings on values. 
 * @author lkastler
 *
 */
public interface Scale {

	/**
	 * scales given <code>value</code> to an integer value.
	 * @param value given value to scale.
	 * @return scaled value or <code>-1</code> if value is not in needed interval.
	 */
	public int scaleInt(int value);
	
	/**
	 * scales given <code>value</code> to a float value
	 * @param value given value to scale
	 * @return scaled value or <code>-1</code> if value could not be scaled
	 */
	public float scaleFloat(float value);
}
