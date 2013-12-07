package edu.uci.ics.githubuserskills;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.controller.MongoDBDataRetriever;
import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.lucene.LuceneUtils;
import edu.uci.ics.githubuserskills.ranking.DirectLuceneBasedUserRankingCreator;
import edu.uci.ics.githubuserskills.ranking.UserRankingCreator;
import edu.uci.ics.githubuserskills.ranking.UserRankingExporter;

public class Launcher {

	private static final Logger console = LoggerFactory.getLogger("console");

	public Launcher() {
	}

	public static void main(String[] args) {
		console.info("Launching Github Profiler...");
		UserRankingCreator rankingCreator = new UserRankingExporter(new DirectLuceneBasedUserRankingCreator());

		MongoDBDataRetriever dataRetriever = new MongoDBDataRetriever(rankingCreator);

		try {
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
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserRankingCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			console.info("Github Profiler shut down.");
		}
	}

	private static List<String> collectUsersAlreadyRanked() {
		File dir = new File(LuceneUtils.getUserRankingsDirectory());
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
