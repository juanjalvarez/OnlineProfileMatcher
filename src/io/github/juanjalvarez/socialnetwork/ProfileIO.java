package io.github.juanjalvarez.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Provides a number of methods that provide IO funcitonality with profiles.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class ProfileIO {

	public static final String PROFILE_DIRECTORY_STRING = "profiles";
	public static final String PROFILE_DELIMITER = "|";

	/**
	 * Loads all of the profiles stored in the file with the given name.
	 * 
	 * @param fileName
	 *            Name of the file to load profiles from.
	 * @return List of profiles that have been loaded from the file with the
	 *         given name.
	 * @throws IOException
	 *             If the file does not exist or if there is a problem reading
	 *             from the file.
	 */
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

	/**
	 * Saves the given list of profiles to a file with the given name.
	 * 
	 * @param fileName
	 *            Name of the file to save the given profiles in.
	 * @param profiles
	 *            List of profiles to save in the file with the given name.
	 * @throws IOException
	 *             If there is a problem writing to the file.
	 */
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