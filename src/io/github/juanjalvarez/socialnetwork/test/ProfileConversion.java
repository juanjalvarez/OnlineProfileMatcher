package io.github.juanjalvarez.socialnetwork.test;

import java.io.File;

import io.github.juanjalvarez.socialnetwork.ProfileFactory;

public class ProfileConversion {

	public static void main(String[] args) throws Exception {
		ProfileFactory.convertFromFacebookAltered("profiles_facebook_altered\\fb.data");
		ProfileFactory.convertFromVkAltered("profiles_vk_altered\\vk.data");
		File[] twiList = new File("profiles_twitter_famous").listFiles();
		for (File f : twiList)
			ProfileFactory.convertFromTwitter(f.getAbsolutePath());
	}
}