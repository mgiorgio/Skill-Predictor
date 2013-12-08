package edu.uci.ics.githubuserskills.lucene;

import java.io.File;

import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final String RANKINGS = "rankings";

	public static final String CONFIG_FILENAME = "config.xml";

	public static final String USERDATA = "userdata";

	public static final String DATA = "data";

	public static final String CONFIG = "cfg";

	public static final String EXECUTION_TOKEN = String.valueOf(System.currentTimeMillis());

	public static final Version LUCENE_VERSION = Version.LUCENE_46;

	private static final Logger console = LoggerFactory.getLogger("console");

	private static final Logger log = LoggerFactory.getLogger(Utils.class);

	private Utils() {
	}

	public static String getFileDirectoryForUser(String user) {
		return getExecutionDirectory() + File.separator + user;
	}

	public static String getExecutionDirectory() {
		return USERDATA + File.separator + Utils.EXECUTION_TOKEN;
	}

	public static String getUserRankingsDirectory() {
		return USERDATA + File.separator + RANKINGS;
	}

	public static class Globals {

		public static final String CONTENTS_FIELD = "contents";
		public static final String AUTHOR_FIELD = "author";
		public static final String TYPE_FIELD = "type";
		public static final String TIME_FIELD = "time";
	}
}
