package edu.uci.ics.githubuserskills;

import edu.uci.ics.githubuserskills.controller.MongoDBToLuceneConverter;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.indexing.IndexingException;

public class Launcher {

	public Launcher() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		MongoDBToLuceneConverter dataConverter = new MongoDBToLuceneConverter();

		try {
			dataConverter.convert();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
