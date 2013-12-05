package edu.uci.ics.githubuserskills;

import java.net.UnknownHostException;

import edu.uci.ics.githubuserskills.controller.LuceneRankingCreator;
import edu.uci.ics.githubuserskills.controller.MongoDBToRawSkillDataIndexer;
import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.indexing.IndexingException;

public class Launcher {

	public Launcher() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		MongoDBToRawSkillDataIndexer dataConverter = new MongoDBToRawSkillDataIndexer();
		
		LuceneRankingCreator rankingCreator = new LuceneRankingCreator();

		dataConverter.initialize();

		try {
			dataConverter.convert();
			
			rankingCreator.rankings();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserRankingCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
