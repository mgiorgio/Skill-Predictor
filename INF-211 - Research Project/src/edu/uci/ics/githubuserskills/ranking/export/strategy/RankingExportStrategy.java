package edu.uci.ics.githubuserskills.ranking.export.strategy;

import edu.uci.ics.githubuserskills.ranking.UserRanking;

/**
 * @author matias
 *
 */
public interface RankingExportStrategy {

	public boolean exportable(UserRanking ranking);

}