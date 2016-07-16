package io.github.juanjalvarez.socialnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ComparisonThread implements Runnable {

	private Thread thread;
	private ProfileMatcher matcher;
	private ComparisonAlgorithm algorithm;
	private Profile[] profileList1;
	private Profile[] profileList2;
	private int startIndex;
	private int endIndex;
	private boolean singleList;
	private boolean finished;

	private static HashMap<String, HashMap<String, HashMap<String, String>>> report;
	public static PrintWriter pw;

	static {
		report = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		try {
			File reportDir = new File("logs");
			if (!reportDir.exists())
				reportDir.mkdir();
			pw = new PrintWriter(new File(reportDir.getName() + "\\report" + System.currentTimeMillis() + ".csv"));
			pw.println("user1,user2,algorithm,field1,field2,similarity");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ComparisonThread(ProfileMatcher pm, ComparisonAlgorithm alg, Profile[] list1, Profile[] list2, int start,
			int end) {
		finished = false;
		matcher = pm;
		algorithm = alg;
		profileList1 = list1;
		profileList2 = list2;
		startIndex = start;
		endIndex = end;
		thread = new Thread(this);
		thread.start();
	}

	public ComparisonThread(ProfileMatcher pm, ComparisonAlgorithm alg, Profile[] list, int start, int end) {
		this(pm, alg, list, list, start, end);
		singleList = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void run() {
		try {
			int profileX, profileY, attributeX, attributeY;
			Profile profile1, profile2;
			double totalScore, curScore;
			String username1, username2;
			Field[] attributeList1, attributeList2;
			Field curField1, curField2;
			for (profileX = startIndex; profileX < endIndex; profileX++) {
				for (profileY = singleList ? profileX + 1 : 0; profileY < endIndex; profileY++) {
					/**
					 * REPORT
					 */
					username1 = profileList1[profileX].getField("username").getValue();
					username2 = profileList2[profileY].getField("username").getValue();
					if (report.get(username1) == null)
						report.put(username1, new HashMap<String, HashMap<String, String>>());
					if (report.get(username1).get(username2) == null)
						report.get(username1).put(username2, new HashMap<String, String>());
					/**
					 * END OF REPORT
					 */
					totalScore = 0.0;
					profile1 = profileList1[profileX];
					profile2 = profileList2[profileY];
					attributeList1 = profile1.getFieldByType(FieldType.STRING);
					attributeList2 = profile2.getFieldByType(FieldType.STRING);
					if (attributeList1.length == 0 || attributeList2.length == 0)
						continue;
					for (attributeX = 0; attributeX < attributeList1.length; attributeX++)
						for (attributeY = 0; attributeY < attributeList2.length; attributeY++) {
							curField1 = attributeList1[attributeX];
							curField2 = attributeList2[attributeY];
							curScore = algorithm.compareLowercase(curField1.getValue(), curField2.getValue());
							if (!curField1.getName().equals(curField2.getName()))
								curScore /= (attributeList1.length * attributeList2.length - 1) * 2;
							totalScore += curScore;
							/**
							 * REPORT
							 */
							synchronized (report) {
								report.get(username1).get(username2).put(algorithm.getName(),
										String.format("%s,%s,%.2f", attributeList1[attributeX],
												attributeList2[attributeX], curScore));
							}
							/**
							 * END OF REPORT
							 */
						}

					attributeList1 = profile1.getFieldByType(FieldType.LOCALIZATION);
					attributeList2 = profile2.getFieldByType(FieldType.LOCALIZATION);
					if (attributeList1.length == 0 || attributeList2.length == 0)
						continue;
					for (attributeX = 0; attributeX < attributeList1.length; attributeX++)
						for (attributeY = 0; attributeY < attributeList2.length; attributeY++) {
							curField1 = attributeList1[attributeX];
							curField2 = attributeList2[attributeY];
							curScore = algorithm.compareLowercase(curField1.getValue(), curField2.getValue());
							if (!curField1.getName().equals(curField2.getName()) && curScore != 1.0)
								curScore /= (attributeList1.length * attributeList2.length - 1) * 2;
							totalScore += curScore;
							/**
							 * REPORT
							 */
							synchronized (report) {
								report.get(username1).get(username2).put(algorithm.getName(),
										String.format("%s,%s,%.2f", attributeList1[attributeX],
												attributeList2[attributeX], curScore));
							}
							/**
							 * END OF REPORT
							 */
						}
					matcher.proveRelation(profileX, profileY, totalScore);
				}
			}
			synchronized (report) {
				for (String user1 : report.keySet())
					for (String user2 : report.get(user1).keySet())
						for (String similarity : report.get(user1).get(user2).keySet()) {
							pw.println(String.format("%s,%s,%s,%s", user1, user2, algorithm.getName(),
									report.get(user1).get(user2).get(similarity)));
						}
			}
			finished = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}