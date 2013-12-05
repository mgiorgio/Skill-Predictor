package edu.uci.ics.githubuserskills.controller;

import java.util.List;

import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
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

	public void convert() throws DataAccessException, IndexingException {
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
	 */
	private List<RawSkillData> retrieveSkillDataForLogin(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Obtains a {@link List} of logins from MongoDB.
	 * 
	 * @return A {@link List} of Strings representing the logins.
	 */
	private List<String> retrieveLogins() {
		// TODO Auto-generated method stub
		return null;
	}

}
