package io.github.juanjalvarez.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProfileIO {

	public static final String PROFILE_DIRECTORY_STRING = "profiles";
	public static final String PROFILE_DELIMITER = "|";

	public static Profile[] loadProfiles(String fileName) throws IOException {
		File f = new File(PROFILE_DIRECTORY_STRING + "\\" + fileName);
		if (!f.exists())
			throw new IOException(String.format("'%s' does not exist", fileName));
		BufferedReader br = new BufferedReader(new FileReader(f));
		br.readLine();
		ArrayList<Profile> list = new ArrayList<Profile>();
		Profile cur = new Profile();
		String line;
		int lineNumber = 0;
		while (true) {
			line = br.readLine();
			lineNumber++;
			if (line == null) {
				break;
			} else if (line.equals(PROFILE_DELIMITER)) {
				list.add(cur);
				cur = new Profile();
			} else {
				try {
					cur.addField(Field.parseField(line));
				} catch (Exception e) {
					System.out.println(lineNumber);
					e.printStackTrace();
					continue;
				}
			}
		}
		br.close();
		return list.toArray(new Profile[list.size()]);
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
}