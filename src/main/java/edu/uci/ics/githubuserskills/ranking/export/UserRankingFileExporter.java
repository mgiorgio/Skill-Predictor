package edu.uci.ics.githubuserskills.ranking.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.lucene.Utils;
import edu.uci.ics.githubuserskills.profile.RawSkillDataProcessor;
import edu.uci.ics.githubuserskills.profile.UserDomainRanking;
import edu.uci.ics.githubuserskills.profile.UserProfile;
import edu.uci.ics.githubuserskills.profile.UserRankingEntry;
import edu.uci.ics.githubuserskills.ranking.export.strategy.RankingExportStrategy;

/**
 * Decorator of {@link RawSkillDataProcessor} that exports the
 * {@link UserDomainRanking} before returning the control to the caller method.
 * 
 * @author matias
 * 
 */
public class UserRankingFileExporter {

	private RankingExportStrategy exportStrategy;

	private static final Logger console = LoggerFactory.getLogger("console");

	public UserRankingFileExporter(RankingExportStrategy exportStrategy) {
		this.exportStrategy = exportStrategy;
	}

	public void export(UserProfile userProfile, boolean stats) throws IOException {
		for (UserDomainRanking eachUserRanking : userProfile.getDomainRankings().values()) {
			if (this.exportStrategy.exportable(eachUserRanking)) {
				exportUserRankingDomain(eachUserRanking, stats);
			}
		}
		if (stats) {
			exportInterDomainAnalysis(userProfile);
		}
	}

	private void exportUserRankingDomain(UserDomainRanking domainRanking, boolean stats) throws IOException {
		List<UserRankingEntry> sortedTerms = domainRanking.getSortedTerms();

		String fileName = Utils.getUserProfileResultsFile(domainRanking);
		File exportFile = new File(fileName);

		FileUtils.touch(exportFile);

		FileWriter writer = new FileWriter(exportFile);
		try {
			if (stats) {
				writer.write(String.format("[Score=%.4f]\n", domainRanking.getDomainStats().getScore()));
				writer.write(String.format("[Contribution to portfolio=%.4f]\n", domainRanking.getDomainStats().getProfileWeight()));
			}
			for (UserRankingEntry termFreq : sortedTerms) {
				writer.write(String.format("%s: %s\n", termFreq.getTerm(), termFreq.getFrequency()));
			}
		} finally {
			writer.close();
		}
		console.info("User Profile for [{}:{}] created in [{}].", domainRanking.getAuthor(), domainRanking.getDomain(), fileName);
	}

	private void exportInterDomainAnalysis(UserProfile userProfile) throws IOException {

		StringBuilder builder = new StringBuilder();
		for (UserDomainRanking eachDomainRanking : userProfile.getDomainRankings().values()) {
			if (eachDomainRanking.getDomainStats().getProfileWeight() > 0) {
				builder.append(String.format("%s=%.4f]\n", eachDomainRanking.getDomain(), eachDomainRanking.getDomainStats().getProfileWeight()));
			}
		}

		if (builder.length() > 0) {

			String fileName = Utils.getUserInterDomainResultsFile(userProfile.getUser());
			File exportFile = new File(fileName);

			FileUtils.touch(exportFile);

			FileWriter writer = new FileWriter(exportFile);

			try {
				writer.write(builder.toString());
			} finally {
				writer.close();
			}
		}

	}
};