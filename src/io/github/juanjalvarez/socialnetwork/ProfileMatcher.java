package io.github.juanjalvarez.socialnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Compares profiles and analyzes their similarity scores in order to identify
 * matches.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class ProfileMatcher {

	private double[][] similarityMap;
	private boolean singleList;
	private Profile[] list1;
	private Profile[] list2;
	private PrintWriter reportWriter;

	/**
	 * Constructs the profile matcher with the two given pools of profiles to
	 * compare in.
	 * 
	 * @param l1
	 *            First pool of profiles.
	 * @param l2
	 *            Second pool of profiles.
	 */
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

	/**
	 * Constructs the profile in such a way where it will only compare profiles
	 * from the same given poo.
	 * 
	 * @param list
	 *            Pool of profiles to compare profiels in.
	 */
	public ProfileMatcher(Profile[] list) {
		this(list, list);
		singleList = true;
	}

	/**
	 * Increases the similarity score between the profiles at the given x index
	 * of the first pool and y index of the second pool by the given amount.
	 * 
	 * @param alg
	 *            String comparison algorithm that calculated the score in
	 *            context.
	 * @param x
	 *            X index of the first pool of profiles.
	 * @param y
	 *            Y index of the second pool of profiles.
	 * @param score
	 *            Score of the two profiles in their respective indexes
	 *            calculated with the given algorith.
	 */
	public void proveRelation(ComparisonAlgorithm alg, int x, int y, double score) {
		similarityMap[x][y] += score;
		reportWriter.println(String.format("%s,%s,%s,%.2f", alg.getName(), list1[x].getField("realname"),
				list2[y].getField("realname"), score));
	}

	/**
	 * Compares the profiles from the first pool with the profiles in the second
	 * pool unless the matcher was constructed to compare from within the same
	 * pool.
	 */
	private void compareProfiles() {
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

	/**
	 * Compares the profiles from the first pool of profiles with the second
	 * pool of profiles and selects the matches by analyzing the yielded
	 * similarity scores.
	 * 
	 * @return List of matches that were identified.
	 */
	public ProfileMatch[] match() {
		try {
			reportWriter = new PrintWriter("report.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		compareProfiles();
		reportWriter.close();
		int x, y;
		int[] maxIdxA = new int[list1.length];
		int[] maxIdxB = new int[list2.length];
		for (x = 0; x < list1.length; x++) {
			maxIdxA[x] = 0;
			for (y = 0; y < list2.length; y++)
				if (similarityMap[x][maxIdxA[x]] < similarityMap[x][y])
					maxIdxA[x] = y;
		}
		for (x = 0; x < list2.length; x++) {
			maxIdxB[x] = 0;
			for (y = 0; y < list1.length; y++)
				if (similarityMap[maxIdxB[x]][x] < similarityMap[y][x])
					maxIdxB[x] = y;
		}
		ArrayList<ProfileMatch> matchList = new ArrayList<ProfileMatch>();
		for (x = 0; x < maxIdxA.length; x++)
			for (y = singleList ? x + 1 : 0; y < maxIdxB.length; y++)
				if (maxIdxA[x] == y && maxIdxB[y] == x)
					matchList.add(new ProfileMatch(list1[x], list2[y], similarityMap[x][y]));

		try {
			File f = new File("similarity.csv");
			PrintWriter pw = new PrintWriter(f);
			pw.print(",");
			for (x = 0; x < list2.length; x++)
				pw.print(list2[x].getField("username").getValue() + ",");
			pw.println();
			for (x = 0; x < similarityMap.length; x++) {
				pw.print(list1[x].getField("username").getValue() + ",");
				for (y = 0; y < similarityMap[x].length; y++)
					pw.print(similarityMap[x][y] + ",");
				pw.println();
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return matchList.toArray(new ProfileMatch[matchList.size()]);
	}
}