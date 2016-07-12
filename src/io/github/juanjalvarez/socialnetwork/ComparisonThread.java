package io.github.juanjalvarez.socialnetwork;

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

	@Override
	public void run() {
		int x, y, z, w;
		Profile a, b;
		double score;
		for (x = startIndex; x < endIndex; x++) {
			for (y = singleList ? x + 1 : 0; y < endIndex; y++) {
				score = 0.0;
				a = profileList1[x];
				b = profileList2[y];
				Field[] string1 = a.getFieldByType(FieldType.STRING);
				Field[] string2 = b.getFieldByType(FieldType.STRING);
				for (z = 0; z < string1.length; z++)
					for (w = 0; w < string2.length; w++)
						score += algorithm.compare(string1[z].getValue(), string2[w].getValue()) - 0.5;
				Field[] local1 = a.getFieldByType(FieldType.LOCALIZATION);
				Field[] local2 = b.getFieldByType(FieldType.LOCALIZATION);
				for (z = 0; z < string1.length; z++)
					for (w = 0; w < string2.length; w++)
						score += algorithm.compare(local1[z].getValue(), local2[w].getValue()) - 0.5;
				matcher.proveRelation(x, y, score);
			}
		}
		finished = true;
	}
}