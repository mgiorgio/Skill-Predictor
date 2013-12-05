package edu.uci.ics.githubuserskills.lucene;

import java.io.File;

public class LuceneUtils {

	public static final String EXECUTION_TOKEN = String.valueOf(System.currentTimeMillis());

	private LuceneUtils() {
	}

	public static String getFileDirectoryForUser(String user) {
		return getLuceneDirectory() + File.separator + user;
	}

	public static String getLuceneDirectory() {
		return "data" + File.separator + LuceneUtils.EXECUTION_TOKEN;
	}

	public static class Globals {

		public static final String CONTENTS_FIELD = "contents";
		public static final String AUTHOR_FIELD = "author";
		public static final String TYPE_FIELD = "type";
		public static final String TIME_FIELD = "time";
	}
}
