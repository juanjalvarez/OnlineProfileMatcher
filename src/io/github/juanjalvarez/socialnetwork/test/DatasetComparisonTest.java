package io.github.juanjalvarez.socialnetwork.test;

import io.github.juanjalvarez.socialnetwork.Profile;
import io.github.juanjalvarez.socialnetwork.ProfileIO;
import io.github.juanjalvarez.socialnetwork.ProfileMatch;
import io.github.juanjalvarez.socialnetwork.ProfileMatcher;

public class DatasetComparisonTest {

	public static void main(String[] arguments) throws Exception {
		Profile[] a = ProfileIO.loadProfiles("a.data");
		Profile[] b = ProfileIO.loadProfiles("b.data");
		Profile[] single = ProfileIO.loadProfiles("Linus Torvalds.data");
		Profile[] fb = ProfileIO.loadProfiles("fb.data");
		Profile[] vk = ProfileIO.loadProfiles("vk.data");
		ProfileMatcher pm = new ProfileMatcher(a, b);
		ProfileMatch[] matchList = pm.match();
		int x, y;
		ProfileMatch tmp;
		for (x = 0; x < matchList.length; x++)
			for (y = x + 1; y < matchList.length; y++)
				if (matchList[x].getRelation() > matchList[y].getRelation()) {
					tmp = matchList[x];
					matchList[x] = matchList[y];
					matchList[y] = tmp;
				}
		for (ProfileMatch match : matchList)
			System.out.println(match.toString());
		System.out.println(String.format("Identified %d matches", matchList.length));
	}

}
