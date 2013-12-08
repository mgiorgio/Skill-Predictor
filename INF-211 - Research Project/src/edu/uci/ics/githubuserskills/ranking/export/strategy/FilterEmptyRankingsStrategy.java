package edu.uci.ics.githubuserskills.ranking.export.strategy;

import edu.uci.ics.githubuserskills.ranking.UserRanking;

public class FilterEmptyRankingsStrategy implements RankingExportStrategy {

	@Override
	public boolean exportable(UserRanking ranking) {
		return !ranking.getTerms().isEmpty();
	}

}
