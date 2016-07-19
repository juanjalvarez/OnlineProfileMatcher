package io.github.juanjalvarez.socialnetwork.test;

import io.github.juanjalvarez.socialnetwork.ComparisonAlgorithm;

public class TimeBenchmark {

	public static void main(String[] arguments) {
		for (ComparisonAlgorithm ca : ComparisonAlgorithm.LIST_OF_ALGORITHMS)
			new ComparisonAlgorithmBenchmarkThread(ca);
	}
}