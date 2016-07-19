package io.github.juanjalvarez.socialnetwork.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.github.juanjalvarez.socialnetwork.ComparisonAlgorithm;

public class StringSimilarityTest {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the first string");
		String a = br.readLine();
		System.out.println("Please enter the second string");
		String b = br.readLine();
		br.close();
		for (ComparisonAlgorithm ca : ComparisonAlgorithm.LIST_OF_ALGORITHMS)
			System.out.println(String.format("'%s' normal scored %.3f", ca.getName(), ca.compare(a, b)));
	}
}
