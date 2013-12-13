package edu.uci.ics.githubuserskills;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.controller.MongoDBDataRetriever;
import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.lucene.Utils;
import edu.uci.ics.githubuserskills.profile.DictionaryManager;
import edu.uci.ics.githubuserskills.profile.DirectLuceneBasedUserProfileCreator;
import edu.uci.ics.githubuserskills.profile.ProfileTotalTermFrequencyCalculator;
import edu.uci.ics.githubuserskills.profile.RawSkillDataProcessor;
import edu.uci.ics.githubuserskills.profile.UserProfile;
import edu.uci.ics.githubuserskills.profile.UserRankingCollector;
import edu.uci.ics.githubuserskills.ranking.export.UserRankingFileExporter;
import edu.uci.ics.githubuserskills.ranking.export.strategy.FilterEmptyRankingsStrategy;
import edu.uci.ics.githubuserskills.ranking.stats.ProfileStatsCalculator;

/**
 * @author matias
 * 
 */
public class Launcher {

	private static final Logger console = LoggerFactory.getLogger("console");

	public Launcher() {
	}

	public static void main(String[] args) {
		console.info("Launching Github Profiler...");

		try {
			// FIXME Implement Chain of Responsibility.
			DirectLuceneBasedUserProfileCreator directLuceneRankingCreator = buildLuceneProfileCreator();
			ProfileTotalTermFrequencyCalculator totalTermFreqCalculator = buildTotalTermFrequencyCalculator(directLuceneRankingCreator);

			UserRankingCollector userRankingCollector = new UserRankingCollector(totalTermFreqCalculator);

			MongoDBDataRetriever dataRetriever = new MongoDBDataRetriever(userRankingCollector);
			dataRetriever.initialize();

			if (isFixedNumberOfUsers(args)) {
				String[] authors = authorsInArguments(args);
				console.info("Profiling authors {}", StringUtils.join(authors));
				dataRetriever.retrieve(Arrays.asList(authors), new LinkedList<String>());
			} else {
				dataRetriever.retrieve();
			}

			// List<String> logins = Arrays.asList("parkr", "mattr-");
			// dataRetriever.retrieve(logins, new ArrayList<String>());

			calculateUserStats(userRankingCollector, totalTermFreqCalculator);

			// printTotalDomainFreqs(totalTermFreqCalculator);

			// printUserProfileStats(userRankingCollector);

			exportAllProfiles(userRankingCollector.getUserProfiles());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserRankingCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			console.info("Github Profiler shut down.");
		}
	}

	private static void exportAllProfiles(Map<String, UserProfile> userProfiles) throws IOException {
		UserRankingFileExporter fileExporter = new UserRankingFileExporter(new FilterEmptyRankingsStrategy());

		for (UserProfile userProfile : userProfiles.values()) {
			fileExporter.export(userProfile, true);
		}
	}

	private static void calculateUserStats(UserRankingCollector userRankingCollector, ProfileTotalTermFrequencyCalculator totalTermFreqCalculator) {
		ProfileStatsCalculator.calculateProfileStats(userRankingCollector.getUserProfiles().values(), totalTermFreqCalculator.getTotalProfile());
	}

	private static ProfileTotalTermFrequencyCalculator buildTotalTermFrequencyCalculator(RawSkillDataProcessor fileExporter) throws IOException {
		return new ProfileTotalTermFrequencyCalculator(fileExporter, DictionaryManager.getDictionaries());
	}

	private static DirectLuceneBasedUserProfileCreator buildLuceneProfileCreator() throws IOException {
		return new DirectLuceneBasedUserProfileCreator(DictionaryManager.getDictionaries());
	}

	private static List<String> collectUsersAlreadyRanked() {
		File dir = new File(Utils.getUserRankingsDirectory());
		List<String> authors = new ArrayList<String>();

		String[] files = dir.list();

		if (files == null) {
			return authors;
		}

		for (int i = 0; i < files.length; i++) {
			authors.add(files[i].substring(0, files[i].lastIndexOf(".")));
		}

		return authors;
	}

	private static String[] authorsInArguments(String[] args) {
		return args;
	}

	private static boolean isFixedNumberOfUsers(String[] args) {
		return args.length > 0;
	}
}
