package eyevisionsearch.logic.strategies.impl.commonstrategy;

import eyevisionsearch.logic.helpers.RowContainer;

/**
 * container that stores a participant representation. Stores the values that were specified by the specific participant at the evaluation.
 * @author lkastler
 *
 */
public class CommonContainer extends RowContainer {
	String id;
	String group;
	int age;
	String gender;
	String profession;
	float exp;
	int frq;
	String comment;
	
	public CommonContainer(String id, String group, int age, String gender, String profession, float exp,
			int frq, String comment) {
		super();
		this.id = id;
		this.group = group;
		this.age = age;
		this.gender = gender;
		this.profession = profession;
		this.exp = exp;
		this.frq = frq;
		this.comment = comment;
	}
	
	@Override
	public Object[] rowToArray() {
		Object[] obj = new Object[8];
		obj[0] = id;
		obj[1] = group;
		obj[2] = age;
		obj[3] = gender;
		obj[4] = profession;
		obj[5] = exp;
		obj[6] = frq;
		obj[7] = comment;
		return obj;
	}

	/**
	 * returns the ID stored in this CommonContainer.
	 * @return the ID stored in this CommonContainer.
	 */
	public String getId() {
		return id;
	}

	/**
	 * returns the group stored in this CommonContainer.
	 * @return the group stored in this CommonContainer.
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * returns the age stored in this CommonContainer.
	 * @return the age stored in this CommonContainer.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * returns the gender stored in this CommonContainer, 'w' for women, 'm' for men.
	 * @return the gender stored in this CommonContainer, 'w' for women, 'm' for men.
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * returns the profession stored in this CommonContainer.
	 * @return the profession stored in this CommonContainer.
	 */
	public String getProfession() {
		return profession;
	}


	/**
	 * returns the experience with image search engines stored in this CommonContainer, from 0 to 300.
	 * @return the experience with image search engines stored in this CommonContainer, from 0 to 300.
	 */
	public float getImageSearchExperience() {
		return exp;
	}

	/**
	 * returns the frequency of image search engines usage stored in this CommonContainer, from 0 to 300.
	 * @return the frequency of image search engines usage stored in this CommonContainer, from 0 to 300.
	 */
	public int getImageSearchFrequency() {
		return frq;
	}


	/**
	 * returns the comment stored in this CommonContainer.
	 * @return the comment stored in this CommonContainer.
	 */
	public String getComment() {
		return comment;
	}
}
