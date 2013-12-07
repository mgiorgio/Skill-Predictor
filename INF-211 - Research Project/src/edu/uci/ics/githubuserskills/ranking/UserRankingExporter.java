package edu.uci.ics.githubuserskills.ranking;

import java.io.IOException;
import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * Decorator of {@link UserRankingCreator} that exports the {@link UserRanking}
 * before returning the control to the caller method.
 * 
 * @author matias
 * 
 */
public class UserRankingExporter implements UserRankingCreator {

	private UserRankingCreator innerUserRankingCreator;

	public UserRankingExporter(UserRankingCreator rankingCreator) {
		this.innerUserRankingCreator = rankingCreator;
	}

	@Override
	public UserRanking rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		UserRanking ranking = this.innerUserRankingCreator.rank(author, rawSkillDataObjects);

		try {
			ranking.exportTextFile();
		} catch (IOException e) {
			throw new UserRankingCreationException(e);
		}

		return ranking;
	}

	@Override
	public void initialize() throws UserRankingCreationException {
		this.innerUserRankingCreator.initialize();
	}

	@Override
	public void close() throws UserRankingCreationException {
		this.innerUserRankingCreator.close();
	}

}
