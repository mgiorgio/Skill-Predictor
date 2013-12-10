package edu.uci.ics.githubuserskills;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import edu.uci.ics.githubuserskills.profile.UserDomainRanking;
import edu.uci.ics.githubuserskills.profile.UserProfile;
import edu.uci.ics.githubuserskills.profile.UserRankingCollector;
import edu.uci.ics.githubuserskills.profile.UserRankingEntry;
import edu.uci.ics.githubuserskills.ranking.export.UserRankingFileExporter;
import edu.uci.ics.githubuserskills.ranking.export.strategy.FilterEmptyRankingsStrategy;
import edu.uci.ics.githubuserskills.ranking.stats.IntraDomainStatsCalculator;

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

			if (isSingleAuthor(args)) {
				String author = authorInArguments(args);
				console.info("Profiling author {}", author);
				dataRetriever.retrieve(author);
			} else {
				List<String> rankedAuthors = collectUsersAlreadyRanked();
				console.info("Profiling all authors ({} excluded)", rankedAuthors.size());
				dataRetriever.retrieveExcluding(rankedAuthors);
			}

			// List<String> logins = Arrays.asList("parkr", "mattr-");
			// dataRetriever.retrieve(logins, new ArrayList<String>());

			calculateIntraDomainStats(userRankingCollector, totalTermFreqCalculator);

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

	private static void calculateIntraDomainStats(UserRankingCollector userRankingCollector, ProfileTotalTermFrequencyCalculator totalTermFreqCalculator) {
		IntraDomainStatsCalculator.calculateIntraDomainStats(userRankingCollector.getUserProfiles().values(), totalTermFreqCalculator.getTotalProfile());
	}

	private static void printUserProfileStats(UserRankingCollector userRankingCollector) {
		for (UserProfile eachUserProfile : userRankingCollector.getUserProfiles().values()) {
			System.out.println("=====User Profile Stats [" + eachUserProfile.getUser() + "]=====");

			for (UserDomainRanking eachRanking : eachUserProfile.getDomainRankings().values()) {
				System.out.println("Expertise [" + eachRanking.getDomain() + "]: " + eachUserProfile.getStats().getDomainIndex(eachRanking.getDomain()).getExpertise());
			}
		}
	}

	private static void printTotalDomainFreqs(ProfileTotalTermFrequencyCalculator totalTermFreqCalculator) {
		UserProfile totalProfile = totalTermFreqCalculator.getTotalProfile();

		for (Entry<String, UserDomainRanking> eachRanking : totalProfile.getDomainRankings().entrySet()) {
			System.out.println("===========" + eachRanking.getValue().getDomain() + "===========");
			for (UserRankingEntry eachTerm : eachRanking.getValue().getSortedTerms()) {
				System.out.println(eachTerm);
			}
		}
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

	private static String authorInArguments(String[] args) {
		return args[0];
	}

	private static boolean isSingleAuthor(String[] args) {
		return args.length > 0;
	}
}
