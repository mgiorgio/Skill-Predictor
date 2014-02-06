/**
 * 
 */
package edu.uci.ics.githubuserskills.ranking.export;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.githubuserskills.lucene.Utils;

/**
 * @author matias
 * 
 */
public class UserMaxScoresCollector {

	private static final String MAXSCORES_LOG = "maxscores.log";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, DomainScore> maxs = new HashMap<String, DomainScore>();

		File directory = getWorkingDir(args);

		List<String> domainFilenames = getDomainFilenames(directory);

		try {
			for (String domainFilename : domainFilenames) {
				Map<String, DomainScore> domainScores;
				domainScores = getDomainScores(directory, domainFilename);

				updateMaxs(maxs, domainScores);
			}

			exportMaxs(directory, maxs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void exportMaxs(File directory, Map<String, DomainScore> maxs) throws IOException {
		File exportFile = new File(directory.getAbsolutePath() + File.separator + MAXSCORES_LOG);

		FileUtils.touch(exportFile);

		FileWriter writer = new FileWriter(exportFile);

		try {
			for (Entry<String, DomainScore> userMaxScores : maxs.entrySet()) {
				writer.write(userMaxScores.getKey());
				writer.write(",");
				writer.write(userMaxScores.getValue().getDomain());
				writer.write(",");
				writer.write(String.format("%.4f\n", userMaxScores.getValue().getScore()));
			}
		} finally {
			writer.close();
		}
	}

	private static void updateMaxs(Map<String, DomainScore> maxs, Map<String, DomainScore> domainScores) {

		for (Entry<String, DomainScore> userScore : domainScores.entrySet()) {
			if (maxs.containsKey(userScore.getKey())) {
				double currentMaxScore = maxs.get(userScore.getKey()).getScore();

				if (currentMaxScore < userScore.getValue().getScore()) {
					maxs.put(userScore.getKey(), userScore.getValue());
				}
			} else {
				maxs.put(userScore.getKey(), userScore.getValue());
			}

		}
	}

	private static File getWorkingDir(String[] args) {
		String dirSufix = "";

		for (int i = 0; i < args.length; i++) {
			dirSufix += File.separator + args[i];
		}

		return new File(Utils.getUserRankingsDirectory() + dirSufix);
	}

	private static Map<String, DomainScore> getDomainScores(File directory, String domainFilename) throws IOException {
		List<String> lines = FileUtils.readLines(new File(directory.getAbsolutePath() + File.separator + domainFilename));
		Map<String, DomainScore> scores = new HashMap<String, DomainScore>();

		String domainName = domainFilename.substring(0, domainFilename.lastIndexOf("-"));

		for (String domainEntry : lines) {
			if (!StringUtils.isEmpty(domainEntry)) {
				int equalIndex = domainEntry.lastIndexOf("=");
				String scoreString = domainEntry.substring(equalIndex + 1);
				double score = Double.parseDouble(scoreString);
				DomainScore domainScore = new DomainScore(domainName, score);
				String username = domainEntry.substring(0, equalIndex);
				scores.put(username, domainScore);
			}
		}

		return scores;
	}

	private static List<String> getDomainFilenames(File directory) {
		String[] list = directory.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".log") && !name.equals(MAXSCORES_LOG);
			}
		});

		return Arrays.asList(list);
	}

	protected static class DomainScore {
		private double score;
		private String domain;

		public DomainScore(String domain, double score) {
			this.domain = domain;
			this.score = score;
		}

		public double getScore() {
			return score;
		}

		public void setScore(double score) {
			this.score = score;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

	}

}
