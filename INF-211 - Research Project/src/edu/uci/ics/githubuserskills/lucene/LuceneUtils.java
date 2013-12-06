package edu.uci.ics.githubuserskills.lucene;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.util.Version;

public class LuceneUtils {

	private static final String FIXED_DICTIONARY_PATH = "fixeddictionary.txt";

	private static final String USERDATA = "userdata";

	public static final String DATA = "data";

	public static final String EXECUTION_TOKEN = String.valueOf(System.currentTimeMillis());

	public static final Version LUCENE_VERSION = Version.LUCENE_46;

	private LuceneUtils() {
	}

	public static String getFileDirectoryForUser(String user) {
		return getLuceneDirectory() + File.separator + user;
	}

	public static String getLuceneDirectory() {
		return USERDATA + File.separator + LuceneUtils.EXECUTION_TOKEN;
	}

	public static String getFixedDictionaryPath() {
		return USERDATA + File.separator + FIXED_DICTIONARY_PATH;
	}

	public static Set<String> loadFixedDictionary() throws IOException {
		List<String> words = FileUtils.readLines(new File(LuceneUtils.getFixedDictionaryPath()));

		return new HashSet<String>(words);
	}

	public static class Globals {

		public static final String CONTENTS_FIELD = "contents";
		public static final String AUTHOR_FIELD = "author";
		public static final String TYPE_FIELD = "type";
		public static final String TIME_FIELD = "time";
	}
}
