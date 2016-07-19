package io.github.juanjalvarez.socialnetwork;

import java.util.ArrayList;

public class ProfileMatcher {

	private double[][] similarityMap;
	private boolean singleList;
	private Profile[] list1;
	private Profile[] list2;

	public ProfileMatcher(Profile[] l1, Profile[] l2) {
		list1 = l1;
		list2 = l2;
		if (list2.length > list1.length) {
			Profile[] tmp = list1;
			list1 = list2;
			list2 = tmp;
		}
		similarityMap = new double[list1.length][list2.length];
		singleList = false;
	}

	public ProfileMatcher(Profile[] list) {
		this(list, list);
		singleList = true;
	}

	public void proveRelation(int x, int y, double score) {
		similarityMap[x][y] += score;
	}

	private void compareProfiles() {
		System.out.println(String.format("Preparing to compare %d times",
				singleList ? list1.length : list1.length + list2.length));
		ArrayList<ComparisonThread> threadList = new ArrayList<ComparisonThread>();
		int cpus = Runtime.getRuntime().availableProcessors(), baseAmount = list1.length / cpus,
				remainder = list1.length % cpus, curRemainder, x, curAmount, startIndex;
		long start = System.currentTimeMillis();
		for (ComparisonAlgorithm alg : ComparisonAlgorithm.LIST_OF_ALGORITHMS) {
			curRemainder = remainder;
			startIndex = 0;
			for (x = 0; x < cpus; x++) {
				curAmount = baseAmount;
				if (curRemainder > 0) {
					curRemainder--;
					curAmount++;
				}
				threadList.add(new ComparisonThread(this, alg, list1, list2, startIndex, startIndex + curAmount));
				startIndex += curAmount;
			}
		}
		for (Thread t : threadList)
			t.start();
		x = 0;
		while (x != threadList.size()) {
			x = 0;
			for (ComparisonThread thread : threadList)
				if (!thread.isAlive())
					x++;
		}
		System.out.println(String.format("It took the system %d seconds to finish matching profiles",
				(System.currentTimeMillis() - start) / 1000));
	}

	public ProfileMatch[] match() {
		compareProfiles();
		int x, y;
		int[] maxIdxA = new int[list1.length];
		int[] maxIdxB = new int[list2.length];
		for (x = 0; x < list1.length; x++) {
			maxIdxA[x] = x;
			for (y = 0; y < list2.length; y++)
				if (maxIdxA[x] < similarityMap[x][y])
					maxIdxA[x] = y;
		}
		if (!singleList)
			for (x = 0; x < list2.length; x++) {
				maxIdxB[x] = x;
				for (y = 0; y < list1.length; y++)
					if (maxIdxB[x] < similarityMap[y][x])
						maxIdxB[x] = y;
			}
		ArrayList<ProfileMatch> matchList = new ArrayList<ProfileMatch>();
		for (x = 0; x < maxIdxA.length; x++)
			for (y = singleList ? x + 1 : 0; y < maxIdxB.length; y++)
				if (maxIdxA[x] == y && maxIdxB[y] == x)
					matchList.add(new ProfileMatch(list1[x], list2[y], similarityMap[x][y]));
		return matchList.toArray(new ProfileMatch[matchList.size()]);
	}
}