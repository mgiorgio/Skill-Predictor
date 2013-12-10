package edu.uci.ics.githubuserskills.ranking;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.ranking.export.strategy.RankingExportStrategy;

/**
 * Decorator of {@link UserRankingCreator} that exports the {@link UserRanking}
 * before returning the control to the caller method.
 * 
 * @author matias
 * 
 */
public class UserRankingExporter implements UserRankingCreator {

	private UserRankingCreator innerUserRankingCreator;

	private RankingExportStrategy exportStrategy;

	public UserRankingExporter(UserRankingCreator rankingCreator, RankingExportStrategy exportStrategy) {
		this.innerUserRankingCreator = rankingCreator;
		this.exportStrategy = exportStrategy;
	}

	@Override
	public Collection<UserRanking> rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		Collection<UserRanking> rankings = this.innerUserRankingCreator.rank(author, rawSkillDataObjects);

		for (UserRanking ranking : rankings) {
			if (this.exportStrategy.exportable(ranking)) {
				try {
					ranking.exportTextFile();
				} catch (IOException e) {
					throw new UserRankingCreationException(e);
				}
			}
		}

		return rankings;
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
