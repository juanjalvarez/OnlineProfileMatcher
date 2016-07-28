package io.github.juanjalvarez.socialnetwork;

import java.util.ArrayList;

/**
 * This class provides the structure of a comparison algorithm that will compare
 * a block of profiles in it's own thread.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class ComparisonThread extends Thread {

	private ProfileMatcher matcher;
	private ComparisonAlgorithm algorithm;
	private Profile[] profileList1;
	private Profile[] profileList2;
	private int startIndex;
	private int endIndex;
	private boolean singleList;

	/**
	 * Constructs the thread with the given origin ProfileMatcher, string
	 * comparison algorithm, profile lists and limiting indexes that define the
	 * block.
	 * 
	 * @param pm
	 *            Origin ProfileMatcher.
	 * @param alg
	 *            String comparison algorithm to be used.
	 * @param list1
	 *            First batch of profiles.
	 * @param list2
	 *            Second batch of profiles.
	 * @param start
	 *            Starting index of the first batch of profiles.
	 * @param end
	 *            Ending index of the first batch of profiles.
	 */
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

	/**
	 * Constructs the thread with the given origin ProfileMatcher, string
	 * comparison algorithm, profile list and limiting indexes that define the
	 * block.
	 * 
	 * @param pm
	 *            Origin ProfileMatcher.
	 * @param alg
	 *            String comparison algorithm to be used.
	 * @param list
	 *            List of profiles to search for matches in.
	 * @param start
	 *            Starting index.
	 * @param end
	 *            Ending index.
	 */
	public ComparisonThread(ProfileMatcher pm, ComparisonAlgorithm alg, Profile[] list, int start, int end) {
		this(pm, alg, list, list, start, end);
		singleList = true;
	}

	@Override
	public void run() {
		try {
			int profileX, profileY, attributeX, attributeY;
			Profile profile1, profile2;
			double totalScore, curScore;
			Field[] attributeList1, attributeList2;
			Field curField1, curField2;
			for (profileX = startIndex; profileX < endIndex; profileX++) {
				for (profileY = singleList ? profileX + 1 : 0; profileY < profileList2.length; profileY++) {
					profile1 = profileList1[profileX];
					profile2 = profileList2[profileY];
					FieldType[] typeList = { FieldType.STRING, FieldType.LOCALIZATION };
					ArrayList<Double> score = new ArrayList<Double>();
					totalScore = 0.0;
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
								totalScore += curScore;
								score.add(curScore);
							}
					}
					matcher.proveRelation(algorithm, profileX, profileY, totalScore);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}