/**
 * 
 */
package edu.uci.ics.githubuserskills.ranking.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import edu.uci.ics.githubuserskills.config.ConfigurationUtils;
import edu.uci.ics.githubuserskills.lucene.Utils;
import edu.uci.ics.githubuserskills.model.SkillProfile;

/**
 * This is an ad-hoc tool that puts all the expertise scores into a single file.
 * 
 * @author matias
 * 
 */
public class ExpertiseAggregatorTool {

	public static void main(String[] args) {
		try {
			File resultsDirectory = new File(Utils.getUserRankingsDirectory());

			createOutputExpertiseFiles();

			String[] allUserResults = resultsDirectory.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".txt");
				}
			});

			for (int i = 0; i < allUserResults.length; i++) {
				String eachResult = allUserResults[i];
				appendExpertise(eachResult);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	private static void appendExpertise(String userScoresFilename) throws IOException {
		// Determine domain.
		int lastHyphen = userScoresFilename.lastIndexOf("-");
		String user = userScoresFilename.substring(0, lastHyphen);
		String domain = userScoresFilename.substring(lastHyphen + 1, userScoresFilename.lastIndexOf("."));

		// Get the first line.
		BufferedReader reader = new BufferedReader(new FileReader(userScoresFilename));
		String expertiseLine = reader.readLine();
		reader.close();

		String score = stripScoreFromExpertiseLine(expertiseLine);

		FileWriter writer = createFileWriterForDomain(domain);
		writer.append(user).append("=").append(score).append("\n");
		writer.close();
	}

	private static String stripScoreFromExpertiseLine(String expertiseLine) {
		// Format: [Expertise=Score]
		return expertiseLine.substring(expertiseLine.lastIndexOf("="), expertiseLine.length() - 1);
	}

	private static FileWriter createFileWriterForDomain(String domain) throws IOException {
		return new FileWriter(getScoresOutputFileFor(domain));
	}

	private static void createOutputExpertiseFiles() throws IOException {
		for (SkillProfile eachDomain : ConfigurationUtils.getConfiguredProfiles()) {
			FileUtils.touch(getScoresOutputFileFor(eachDomain.getName()));
		}
	}

	private static File getScoresOutputFileFor(String domain) {
		return new File(domain + "-scores.txt");
	}
}
