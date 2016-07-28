package io.github.juanjalvarez.socialnetwork;

/**
 * Stores two profiles that have been identified as a match along with their
 * similarity score.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class ProfileMatch {

	private Profile a;
	private Profile b;
	private double relation;

	/**
	 * Constructs the match with the two given profiles and their similarity
	 * score.
	 * 
	 * @param a
	 *            First profile.
	 * @param b
	 *            Second profile.
	 * @param relation
	 *            The similarity score between the two given profiles.
	 */
	public ProfileMatch(Profile a, Profile b, double relation) {
		this.a = a;
		this.b = b;
		this.relation = relation;
	}

	/**
	 * Returns the first profile.
	 * 
	 * @return First profile.
	 */
	public Profile getA() {
		return a;
	}

	/**
	 * Returns the second profile.
	 * 
	 * @return Second profile.
	 */
	public Profile getB() {
		return b;
	}

	/**
	 * Returns the similarity score of the two given profiles.
	 * 
	 * @return Similarity score of the two given profiles.
	 */
	public double getRelation() {
		return relation;
	}

	@Override
	public String toString() {
		return String.format("[%s] <=> [%s] = %.2f", a.getField("realname").getValue(),
				b.getField("realname").getValue(), relation);
	}
}
