package edu.uci.ics.githubuserskills.ranking.export.strategy;

import edu.uci.ics.githubuserskills.profile.UserDomainRanking;

/**
 * @author matias
 *
 */
public interface RankingExportStrategy {

	public boolean exportable(UserDomainRanking ranking);

}