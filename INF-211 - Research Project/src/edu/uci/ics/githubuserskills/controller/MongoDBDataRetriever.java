package edu.uci.ics.githubuserskills.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.dataAccess.AuthorAndUserDAO;
import edu.uci.ics.githubuserskills.dataAccess.DataAccessException;
import edu.uci.ics.githubuserskills.dataAccess.DataAggregator;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;
import edu.uci.ics.githubuserskills.ranking.UserRankingCreator;

public class MongoDBDataRetriever {

	private static final Logger console = LoggerFactory.getLogger("console");

	private UserRankingCreator userRankingCreator;

	public MongoDBDataRetriever(UserRankingCreator indexer) {
		this.setUserRankingCreator(indexer);
	}

	public void initialize() throws UserRankingCreationException {
		this.getUserRankingCreator().initialize();
	}

	public void retrieve(String author) throws DataAccessException, UserRankingCreationException, UnknownHostException {
		this.retrieve(Arrays.asList(author), new ArrayList<String>());
	}

	public void retrieveExcluding(List<String> exclude) throws DataAccessException, UserRankingCreationException, UnknownHostException {
		this.retrieve(this.retrieveLogins(), exclude);
	}

	public void retrieve() throws DataAccessException, UserRankingCreationException, UnknownHostException {
		this.retrieveExcluding(new ArrayList<String>());
	}

	private void retrieve(List<String> logins, List<String> exclude) throws DataAccessException, UserRankingCreationException, UnknownHostException {
		List<String> include = ListUtils.removeAll(logins, exclude);

		console.info("About to profile {} users...", include.size());
		for (String login : include) {
			console.info("Profiling user {}...", login);

			// Collect the RawSkillData objects associated to each login.
			console.info("Retrieving Raw Skill Data from Github for {}...", login);
			List<RawSkillData> rawDataForLogin = this.retrieveSkillDataForLogin(login);

			// Index the obtained objects.
			console.info("Creating User Ranking for {}...", login);
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
