package io.github.juanjalvarez.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProfileIO {

	public static final String PROFILE_DIRECTORY_STRING = "profiles";
	public static final String PROFILE_DELIMITER = "|";

	public static Profile[] loadProfiles(String fileName) throws IOException {
		File f = new File(PROFILE_DIRECTORY_STRING + "\\" + fileName);
		if (!f.exists())
			throw new IOException(String.format("'%s' does not exist", fileName));
		BufferedReader br = new BufferedReader(new FileReader(f));
		int quantity = Integer.parseInt(br.readLine()), x = 0;
		Profile[] list = new Profile[quantity];
		Profile cur = new Profile();
		String line;
		while (true) {
			line = br.readLine();
			if (line == null) {
				break;
			} else if (line.equals(PROFILE_DELIMITER)) {
				list[x++] = cur;
				cur = new Profile();
			} else {
				cur.addField(Field.parseField(line));
			}
		}
		br.close();
		return list;
	}

	public static void saveProfiles(String fileName, Profile[] profiles) throws IOException {
		File f = new File(PROFILE_DIRECTORY_STRING + "\\" + fileName);
		PrintWriter pw = new PrintWriter(f);
		pw.println(profiles.length);
		for (int x = 0; x < profiles.length; x++) {
			pw.println(profiles[x]);
			pw.println(PROFILE_DELIMITER);
		}
		pw.close();
	}

	public static void test(String[] args) throws Exception {
		Profile p = new Profile();
		p.addField(new Field(FieldType.ID, "id", "100"));
		Profile[] arr = { p, p, p, p, p, p, p };
		saveProfiles("test.profilecluster", arr);
		Profile[] arr2 = loadProfiles("test.profilecluster");
		for (Profile profile : arr2)
			System.out.println(profile.toString() + "\n");
	}
}