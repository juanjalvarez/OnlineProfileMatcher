package io.github.juanjalvarez.socialnetwork;

public abstract class ComparisonAlgorithm {

	private String name;

	public ComparisonAlgorithm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public double compareLowercase(String a, String b) {
		return compare(a.toLowerCase(), b.toLowerCase());
	}

	public abstract double compare(String a, String b);

	public static final ComparisonAlgorithm[] LIST_OF_ALGORITHMS = {

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
			}, new ComparisonAlgorithm("Cosine Similarity") {

				@Override
				public double compare(String a, String b) {
					return 1.0 - Algorithm.COSINE.distance(a, b);
				}
			}, new ComparisonAlgorithm("N-Gram") {

				@Override
				public double compare(String a, String b) {
					return Algorithm.N_GRAM.distance(a, b);
				}
			}

	};
}