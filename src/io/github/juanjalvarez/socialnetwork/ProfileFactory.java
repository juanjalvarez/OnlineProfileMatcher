package io.github.juanjalvarez.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONObject;

import twitter4j.User;

public class ProfileFactory {

	public static void convertFromTwitter(String fileName) throws Exception {
		File f = new File(fileName);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		@SuppressWarnings("unchecked")
		ArrayList<User> list = (ArrayList<User>) ois.readObject();
		Profile[] arr = new Profile[list.size()];
		User u;
		Profile p;
		for (int x = 0; x < list.size(); x++) {
			u = list.get(x);
			p = new Profile();
			p.addField(new Field(FieldType.ID, "twitter_id", Long.toString(u.getId())));
			p.addField(new Field(FieldType.STRING, "username", u.getScreenName()));
			p.addField(new Field(FieldType.STRING, "realname", u.getName()));
			p.addField(new Field(FieldType.LOCALIZATION, "language", u.getLang()));
			p.addField(new Field(FieldType.LOCALIZATION, "location",
					u.getLocation() == null || u.getLocation().equals("") ? "null" : u.getLocation()));
			p.addField(new Field(FieldType.LOCALIZATION, "timezone", u.getTimeZone()));
			arr[x] = p;
		}
		ProfileIO.saveProfiles(f.getName(), arr);
		ois.close();
	}

	public static void convertFromSynthetic(String fileName) throws Exception {
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		String[] synth;
		ArrayList<Profile> list = new ArrayList<Profile>();
		Profile p;
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			synth = line.split("\\|");
			p = new Profile();
			p.addField(new Field(FieldType.ID, "synthetic_id", synth[0]));
			p.addField(new Field(FieldType.STRING, "username", synth[1]));
			p.addField(new Field(FieldType.STRING, "realname", synth[2]));
			p.addField(new Field(FieldType.CHAR, "gender", synth[3]));
			p.addField(new Field(FieldType.DATE, "birthdate", synth[4]));
			p.addField(new Field(FieldType.STRING, "email", synth[5]));
			p.addField(new Field(FieldType.ID, "phone", synth[6]));
			p.addField(new Field(FieldType.LOCALIZATION, "timezone", synth[9]));
			p.addField(new Field(FieldType.LOCALIZATION, "language", synth[10]));
			p.addField(new Field(FieldType.LOCALIZATION, "location", synth[11] + " " + synth[12]));
			p.addField(new Field(FieldType.STRING_DATA, "description", synth[12]));
			list.add(p);
		}
		br.close();
		ProfileIO.saveProfiles(f.getName(), list.toArray(new Profile[list.size()]));
	}

	public static void convertFromFacebookAltered(String fileName) throws Exception {
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringTokenizer st = new StringTokenizer(br.readLine(), "|");
		br.close();
		ArrayList<Profile> list = new ArrayList<Profile>();
		String cur, id, name;
		String[] split;
		StringBuilder sb;
		Profile p;
		int x;
		while (st.hasMoreTokens()) {
			cur = st.nextToken();
			split = cur.split(" ");
			id = split[0];
			if (split.length > 2) {
				sb = new StringBuilder();
				for (x = 1; x < split.length; x++)
					sb.append(split[x]);
				name = sb.toString();
			} else
				name = split[1];
			p = new Profile();
			p.addField(new Field(FieldType.ID, "facebook_altered_id", id));
			p.addField(new Field(FieldType.STRING, "realname", name));
			list.add(p);
		}
		ProfileIO.saveProfiles(f.getName(), list.toArray(new Profile[list.size()]));
	}

	public static void convertFromVkAltered(String fileName) throws Exception {
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line, id, firstName, secondName;
		JSONObject profileJSON, profileInfoJSON;
		ArrayList<Profile> list = new ArrayList<Profile>();
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			profileJSON = new JSONObject(line);
			id = Long.toString(profileJSON.getLong("_id"));
			profileInfoJSON = profileJSON.getJSONObject("info");
			firstName = profileInfoJSON.getString("name");
			secondName = profileInfoJSON.getString("secName");
			Profile p = new Profile();
			p.addField(new Field(FieldType.ID, "vk_altered_id", id));
			p.addField(new Field(FieldType.STRING, "realname", firstName + " " + secondName));
			list.add(p);
		}
		br.close();
		ProfileIO.saveProfiles(f.getName(), list.toArray(new Profile[list.size()]));
	}

	public static void main(String[] arguments) throws Exception {
		convertFromVkAltered("profiles_vk_altered\\vk.data");
	}
}