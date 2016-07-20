package io.github.juanjalvarez.socialnetwork.test;

import io.github.juanjalvarez.socialnetwork.ComparisonAlgorithm;

public class ComparisonAlgorithmBenchmarkThread implements Runnable {

	private static final int NUMBER_OF_COMPARISONS = 1000000;
	private static final String STRING_DATA = "The quick brown fox jumps over the lazy dog.";

	private Thread thread;
	private ComparisonAlgorithm ca;

	public ComparisonAlgorithmBenchmarkThread(ComparisonAlgorithm alg) {
		thread = new Thread(this);
		ca = alg;
		thread.start();
	}

	public void run() {
		long start = System.currentTimeMillis();
		for (int x = 0; x < NUMBER_OF_COMPARISONS; x++)
			ca.compareLowercase(STRING_DATA, STRING_DATA);
		long end = System.currentTimeMillis();
		System.out.println(String.format("'%s' took %.2f seconds to finish", ca.getName(), (end - start) / 1000.0));
	}
}