package io.github.juanjalvarez.socialnetwork;

public abstract class ComparisonAlgorithm {

	private String name;

	public ComparisonAlgorithm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract double compare(String a, String b);

	public static final ComparisonAlgorithm[] LIST_OF_ALGORITHMS = {

			new ComparisonAlgorithm("LCS String Similarity") {

				@Override
				public double compare(String a, String b) {
					return Algorithms.LCSStringSimilarity(a, b);
				}

			}, new ComparisonAlgorithm("Unique Element Ratio") {

				@Override
				public double compare(String a, String b) {
					return Algorithms.uniqueElementRatio(a, b);
				}

			}, new ComparisonAlgorithm("Element Difference Ratio") {

				@Override
				public double compare(String a, String b) {
					return Algorithms.elementDifferenceRatio(a, b);
				}

			}, new ComparisonAlgorithm("String Cosine Similarity") {

				@Override
				public double compare(String a, String b) {
					return Algorithms.cosineSimilarity(a, b);
				}

			}

	};
}