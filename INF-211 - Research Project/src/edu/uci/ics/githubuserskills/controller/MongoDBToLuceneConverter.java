package edu.uci.ics.githubuserskills.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;

import edu.uci.ics.githubuserskills.dataAccess.AuthorAndUserDAO;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.dataAccess.DataAggregator;
import edu.uci.ics.githubuserskills.indexing.IndexingException;
import edu.uci.ics.githubuserskills.indexing.RawSkillDataIndexer;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;

public class MongoDBToLuceneConverter {

	private RawSkillDataIndexer indexer;

	public MongoDBToLuceneConverter() {
	}

	public void initialize() {
		this.setIndexer(this.createIndexer());
		this.getIndexer().initialize();
	}

	private RawSkillDataIndexer createIndexer() {
		boolean append = Boolean.TRUE.toString().equals(System.getProperty("append"));
		return new RawSkillDataIndexer();
	}

	public void convert() throws DataAccessException, IndexingException, UnknownHostException {
		// Get all the logins.
		List<String> logins = this.retrieveLogins();

		for (String login : logins) {
			// Collect the RawSkillData objects associated to each login.
			List<RawSkillData> rawDataForLogin = this.retrieveSkillDataForLogin(login);

			// Index the obtained objects.
			this.getIndexer().index(rawDataForLogin);
		}

		this.getIndexer().close();

	}

	private RawSkillDataIndexer getIndexer() {
		return indexer;
	}

	private void setIndexer(RawSkillDataIndexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * Retrieves all contributions for a given login. A contribution can be any
	 * element from the {@link SkillDataType} enumeration.
	 * 
	 * @param login
	 * @return The list of contributions.
	 * @throws UnknownHostException 
	 */
	private List<RawSkillData> retrieveSkillDataForLogin(String login) throws UnknownHostException {
		// TODO Auto-generated method stub
		DataAggregator dataAgg = new DataAggregator();
		return(dataAgg.getAuthorData(login));
	}

	/**
	 * Obtains a {@link List} of logins from MongoDB.
	 * 
	 * @return A {@link List} of Strings representing the logins.
	 * @throws UnknownHostException 
	 */
	private List<String> retrieveLogins() throws UnknownHostException {
		// TODO Auto-generated method stub
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		BasicDBList list = dao.getAllLogins();
		//TODO: convert BasicDBList to java List.
		return(null);
	}

}
