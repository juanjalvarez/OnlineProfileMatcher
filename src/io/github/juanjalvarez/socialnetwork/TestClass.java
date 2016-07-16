package io.github.juanjalvarez.socialnetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestClass {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the first string");
		String a = br.readLine();
		System.out.println("Please enter the second string");
		String b = br.readLine();
		double normal = 0.0, lower = 0.0, cur;
		for (ComparisonAlgorithm ca : ComparisonAlgorithm.LIST_OF_ALGORITHMS) {
			cur = ca.compare(a, b);
			System.out.println(String.format("'%s' normal scored %.3f", ca.getName(), cur));
			normal += cur;
			cur = ca.compareLowercase(a, b);
			System.out.println(String.format("'%s' lowercase scored %.3f", ca.getName(), cur));
			lower += cur;
		}
		normal /= ComparisonAlgorithm.LIST_OF_ALGORITHMS.length;
		lower /= ComparisonAlgorithm.LIST_OF_ALGORITHMS.length;
		System.out.println("Normal: " + normal);
		System.out.println("Lowercase: " + lower);
		br.close();
	}
}