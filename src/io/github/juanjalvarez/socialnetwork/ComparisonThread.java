package io.github.juanjalvarez.socialnetwork;

import java.util.ArrayList;

public class ComparisonThread extends Thread {

	private ProfileMatcher matcher;
	private ComparisonAlgorithm algorithm;
	private Profile[] profileList1;
	private Profile[] profileList2;
	private int startIndex;
	private int endIndex;
	private boolean singleList;

	public ComparisonThread(ProfileMatcher pm, ComparisonAlgorithm alg, Profile[] list1, Profile[] list2, int start,
			int end) {
		super();
		matcher = pm;
		algorithm = alg;
		profileList1 = list1;
		profileList2 = list2;
		startIndex = start;
		endIndex = end;
		singleList = false;
	}

	public ComparisonThread(ProfileMatcher pm, ComparisonAlgorithm alg, Profile[] list, int start, int end) {
		this(pm, alg, list, list, start, end);
		singleList = true;
	}

	public void run() {
		try {
			int profileX, profileY, attributeX, attributeY;
			Profile profile1, profile2;
			double totalScore, curScore, maxScore, avgScore;
			Field[] attributeList1, attributeList2;
			Field curField1, curField2;
			for (profileX = startIndex; profileX < endIndex; profileX++) {
				for (profileY = singleList ? profileX + 1 : 0; profileY < profileList2.length; profileY++) {
					profile1 = profileList1[profileX];
					profile2 = profileList2[profileY];
					FieldType[] typeList = { FieldType.STRING, FieldType.LOCALIZATION };
					ArrayList<Double> score = new ArrayList<Double>();
					for (FieldType type : typeList) {
						attributeList1 = profile1.getFieldsByType(type);
						attributeList2 = profile2.getFieldsByType(type);
						if (attributeList1.length == 0 || attributeList2.length == 0)
							continue;
						for (attributeX = 0; attributeX < attributeList1.length; attributeX++)
							for (attributeY = 0; attributeY < attributeList2.length; attributeY++) {
								curField1 = attributeList1[attributeX];
								curField2 = attributeList2[attributeY];
								curScore = algorithm.compare(curField1.getValue(), curField2.getValue());
								score.add(curScore);
							}
					}

					maxScore = 0.0;
					avgScore = 0.0;
					for (double d : score) {
						avgScore += d;
						if (d > maxScore)
							maxScore = d;
					}
					avgScore -= maxScore;
					avgScore /= score.size() - 1;
					totalScore = maxScore - avgScore;
					matcher.proveRelation(profileX, profileY, totalScore);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}