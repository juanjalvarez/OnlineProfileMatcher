package io.github.juanjalvarez.socialnetwork;

/**
 * 
 * This class provides the basic structure of a string comparison algorithm.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public abstract class ComparisonAlgorithm {

	private String name;

	/**
	 * Constructor for the comparison algorithm that gives the algorithm a name
	 * for reference.
	 * 
	 * @param name
	 *            Name of the algorithm being implemented.
	 */
	public ComparisonAlgorithm(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the algorithm in context.
	 * 
	 * @return Name of the algorithm in context.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Compares the two given strings using the algorithm in context except the
	 * two given strings will be converted to their respective lowercase
	 * versions first.
	 * 
	 * @param a
	 *            First string.
	 * @param b
	 *            Second string.
	 * @return Similarity score between the two given strings.
	 */
	public double compareLowercase(String a, String b) {
		return compare(a.toLowerCase(), b.toLowerCase());
	}

	/**
	 * Abstract method intended for comparing the two given strings.
	 * 
	 * @param a
	 *            First string.
	 * @param b
	 *            Second string.
	 * @return Similarity score between the two given strings using the string
	 *         comparison algorithm in context.
	 */
	public abstract double compare(String a, String b);

	/**
	 * A globally accessible list of string comparison algorithms that have
	 * already been implemented.
	 */
	public static ComparisonAlgorithm[] LIST_OF_ALGORITHMS = {

			new ComparisonAlgorithm("Soundex Difference") {

				@Override
				public double compare(String a, String b) {
					String s1 = Algorithm.soundex(a);
					String s2 = Algorithm.soundex(b);
					int score = 0;
					if (s1.charAt(0) == s2.charAt(0))
						score++;
					if (s1.charAt(1) == s2.charAt(1))
						score++;
					if (s1.charAt(2) == s2.charAt(2))
						score++;
					if (s1.charAt(3) == s2.charAt(3))
						score++;
					return score / 4.0;
				}

			}, new ComparisonAlgorithm("LCS String Similarity") {

				@Override
				public double compare(String a, String b) {
					return 1.0 - Algorithm.METRIC_LCS.distance(a, b);
				}

			}, new ComparisonAlgorithm("Normalized Levenshtein") {

				@Override
				public double compare(String a, String b) {
					return 1.0 - Algorithm.LEVENSHTEIN.distance(a, b);
				}

			}, new ComparisonAlgorithm("Jaro Winkler") {

				@Override
				public double compare(String a, String b) {
					return 1.0 - Algorithm.JARO_WINKLER.distance(a, b);
				}

			}, new ComparisonAlgorithm("N-Gram") {

				@Override
				public double compare(String a, String b) {
					return Algorithm.N_GRAM.distance(a, b);
				}

			}, new ComparisonAlgorithm("INHOUSE - Unique Element Ratio") {

				@Override
				public double compare(String a, String b) {
					return Algorithm.uniqueElementRatio(a, b);
				}

			}, new ComparisonAlgorithm("INHOUSE - Element Difference Ratio") {

				@Override
				public double compare(String a, String b) {
					return Algorithm.elementDifferenceRatio(a, b);
				}

			}, new ComparisonAlgorithm("INHOUSE - String Cosine Similarity") {

				@Override
				public double compare(String a, String b) {
					return Algorithm.cosineSimilarity(a, b);
				}

			}, new ComparisonAlgorithm("INHOUSE - Cat Sequence") {

				@Override
				public double compare(String a, String b) {
					return Algorithm.catSequenceAlgorithm(a, b);
				}

			}

	};
}