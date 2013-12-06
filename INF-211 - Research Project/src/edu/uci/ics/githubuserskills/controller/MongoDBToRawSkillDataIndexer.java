package edu.uci.ics.githubuserskills.controller;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.uci.ics.githubuserskills.dataAccess.AuthorAndUserDAO;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.dataAccess.DataAggregator;
import edu.uci.ics.githubuserskills.indexing.IndexingException;
import edu.uci.ics.githubuserskills.indexing.LuceneRawSkillDataIndexer;
import edu.uci.ics.githubuserskills.indexing.RawSkillDataIndexer;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;

public class MongoDBToRawSkillDataIndexer {

	private RawSkillDataIndexer indexer;

	public MongoDBToRawSkillDataIndexer(RawSkillDataIndexer indexer) {
		this.setIndexer(indexer);
	}

	public void initialize() {
		this.getIndexer().initialize();
	}

	private LuceneRawSkillDataIndexer createIndexer() {
		boolean append = Boolean.TRUE.toString().equals(System.getProperty("append"));
		return new LuceneRawSkillDataIndexer(append);
	}

	public void convert() throws DataAccessException, IndexingException, UnknownHostException {
		// Get all the logins.
		List<String> logins = this.retrieveLogins();

		try {
			FileUtils.writeLines(new File("authors.txt"), logins);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);

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

	protected void setIndexer(RawSkillDataIndexer indexer) {
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
		return (dataAgg.getAuthorData(login));
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
		List<String> list = dao.getLoginList();
		// TODO: convert BasicDBList to java List.
		return (list);
	}

}
