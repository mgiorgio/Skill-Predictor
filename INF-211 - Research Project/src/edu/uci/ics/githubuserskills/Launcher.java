package edu.uci.ics.githubuserskills;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.controller.MongoDBDataRetriever;
import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
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
				console.info("Profiling all authors.");
				dataRetriever.retrieve();
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

	private static String authorInArguments(String[] args) {
		return args[0];
	}

	private static boolean isSingleAuthor(String[] args) {
		return args.length > 0;
	}
}
