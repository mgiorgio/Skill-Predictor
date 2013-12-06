package edu.uci.ics.githubuserskills;

import java.net.UnknownHostException;

import edu.uci.ics.githubuserskills.controller.MongoDBDataRetriever;
import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.ranking.DirectLuceneBasedUserRankingCreator;
import edu.uci.ics.githubuserskills.ranking.UserRankingCreator;

public class Launcher {

	public Launcher() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		UserRankingCreator rankingCreator = new DirectLuceneBasedUserRankingCreator();

		MongoDBDataRetriever dataRetriever = new MongoDBDataRetriever(rankingCreator);

		dataRetriever.initialize();

		try {
			dataRetriever.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserRankingCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
