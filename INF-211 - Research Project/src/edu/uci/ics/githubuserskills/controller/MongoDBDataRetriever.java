package edu.uci.ics.githubuserskills.controller;

import java.net.UnknownHostException;
import java.util.List;

import edu.uci.ics.githubuserskills.dataAccess.AuthorAndUserDAO;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.dataAccess.DataAggregator;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;
import edu.uci.ics.githubuserskills.ranking.UserRankingCreator;

public class MongoDBDataRetriever {

	private UserRankingCreator userRankingCreator;

	public MongoDBDataRetriever(UserRankingCreator indexer) {
		this.setUserRankingCreator(indexer);
	}

	public void initialize() {
		this.getUserRankingCreator().initialize();
	}

	public void start() throws DataAccessException, UserRankingCreationException, UnknownHostException {
		// Get all the logins.
		List<String> logins = this.retrieveLogins();

		for (String login : logins) {
			// Collect the RawSkillData objects associated to each login.
			List<RawSkillData> rawDataForLogin = this.retrieveSkillDataForLogin(login);

			// Index the obtained objects.
			this.getUserRankingCreator().rank(login, rawDataForLogin);
		}

		this.getUserRankingCreator().close();

	}

	private UserRankingCreator getUserRankingCreator() {
		return userRankingCreator;
	}

	protected void setUserRankingCreator(UserRankingCreator indexer) {
		this.userRankingCreator = indexer;
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
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		List<String> list = dao.getLoginList();
		// TODO: convert BasicDBList to java List.
		return (list);
	}

}
