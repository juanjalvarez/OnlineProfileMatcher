package io.github.juanjalvarez.socialnetwork.test;

import java.util.Random;

import io.github.juanjalvarez.socialnetwork.ComparisonAlgorithm;

public class ComparisonAlgorithmBenchmarkThread implements Runnable {

	private static final String[] DATA_LIST = new String[1048576];

	static {
		Random r = new Random();
		int x, y;
		char[] newString;
		for (x = 0; x < DATA_LIST.length; x++) {
			newString = new char[10 + r.nextInt(15)];
			for (y = 0; y < newString.length; y++)
				newString[y] = (char) r.nextInt(256);
			DATA_LIST[x] = new String(newString);
		}
	}

	private Thread thread;
	private ComparisonAlgorithm ca;

	public ComparisonAlgorithmBenchmarkThread(ComparisonAlgorithm alg) {
		thread = new Thread(this);
		ca = alg;
		thread.start();
	}

	public void run() {
		long start = System.currentTimeMillis();
		for (String s : DATA_LIST)
			ca.compareLowercase(s, DATA_LIST[0]);
		long end = System.currentTimeMillis();
		System.out.println(String.format("'%s' took %.2f seconds to finish", ca.getName(), (end - start) / 1000.0));
	}
}