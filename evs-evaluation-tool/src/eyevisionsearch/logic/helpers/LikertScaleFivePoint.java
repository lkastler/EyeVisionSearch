package eyevisionsearch.logic.helpers;

/**
 * helper singleton to apply a five-point Likert-Scale on values.
 * @author lkastler
 *
 */
public class LikertScaleFivePoint implements Scale {

	private static LikertScaleFivePoint instance = null;

	private LikertScaleFivePoint() {}
	
	/**
	 * returns the instance of a five-point Likert-Scale Scale.
	 * @return the instance of a five-point Likert-Scale Scale.
	 */
	public static LikertScaleFivePoint instance() {
		if(instance == null)
			instance  = new LikertScaleFivePoint();
		return instance;
	}

	
	@Override
	/**
	 * returns a five-point Likert scale value for given value. 
	 * @param value needs to be between 0 and 300 otherwise -1 is returned.
	 * @returns scaled five-point Likert value or -1 if value is larger than 300 or lower 0.
	 */
	public int scaleInt(int value) {
		if(value < 60)
			return 1;
		else if(value < 120)
			return 2;
		else if(value <= 180)
			return 3;
		else if(value <= 240)
			return 4;
		else if(value <= 300)
			return 5;
		return -1;
	}
	
	

	/**
	 * returns a five-point Likert scale value for given value. 
	 * @param value needs to be between 0 and 300 otherwise -1 is returned.
	 * @return  scaled five-point Likert value or -1 if value is larger than 300 or lower 0.
	 */
	public float scaleFloat(float value) {
		if(value < 0)
			return -1;
		if(value > 300)
			return -1;
		return (value / 75) + 1;
	}

}
