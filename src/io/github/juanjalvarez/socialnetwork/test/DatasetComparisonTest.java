package io.github.juanjalvarez.socialnetwork.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.github.juanjalvarez.socialnetwork.ComparisonAlgorithm;
import io.github.juanjalvarez.socialnetwork.FieldType;
import io.github.juanjalvarez.socialnetwork.Profile;
import io.github.juanjalvarez.socialnetwork.ProfileIO;
import io.github.juanjalvarez.socialnetwork.ProfileMatch;
import io.github.juanjalvarez.socialnetwork.ProfileMatcher;

public class DatasetComparisonTest {

	public static void main(String[] arguments) throws Exception {
		File dir = new File("profiles");
		if (!dir.exists())
			dir.mkdir();
		File[] fList = dir.listFiles();
		System.out.println("Available pools of profiles: ");
		for (File f : fList)
			System.out.println(f.getName());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("What is the file name for the first pool of profiles?");
		String str = br.readLine();
		Profile[] list1 = ProfileIO.loadProfiles(str);
		System.out.println("What is the file name for the second pool of profiles?");
		str = br.readLine();
		Profile[] list2 = ProfileIO.loadProfiles(str);
		br.close();
		int x, y, z;
		ComparisonAlgorithm[] originalList = ComparisonAlgorithm.LIST_OF_ALGORITHMS;
		ArrayList<ComparisonAlgorithm> tmpList;
		boolean only = false;
		boolean benchmark = false;
		for (z = benchmark ? 0 : originalList.length - 1; z < originalList.length; z++) {
			if (benchmark) {
				tmpList = new ArrayList<ComparisonAlgorithm>();
				if (only) {
					tmpList.add(originalList[z]);
					only = false;
				} else {
					for (x = 0; x < originalList.length; x++)
						if (x != z)
							tmpList.add(originalList[x]);
					only = true;
				}
				ComparisonAlgorithm.LIST_OF_ALGORITHMS = tmpList.toArray(new ComparisonAlgorithm[tmpList.size()]);
				System.out.println(String.format("%s %s", only ? "only" : "all-but", originalList[z].getName()));
				if (only)
					z--;
			}
			ProfileMatcher pm = new ProfileMatcher(list1, list2);
			ProfileMatch[] matchList = pm.match();
			ProfileMatch tmp;
			for (x = 0; x < matchList.length; x++)
				for (y = x + 1; y < matchList.length; y++)
					if (matchList[x].getRelation() > matchList[y].getRelation()) {
						tmp = matchList[x];
						matchList[x] = matchList[y];
						matchList[y] = tmp;
					}
			// for (ProfileMatch match : matchList)
			// System.out.println(match.toString());
			int p = 0;
			for (ProfileMatch m : matchList) {
				String id1 = m.getA().getFieldsByType(FieldType.ID)[0].getValue();
				String id2 = m.getB().getFieldsByType(FieldType.ID)[0].getValue();
				if (id1.equals(id2))
					p++;
				else
					System.out.println("BAD MATCH: " + m.toString());
			}
			System.out.println(String.format("Identified %d matches, %d proper matches\n", matchList.length, p));
		}
	}
}
