package edu.uci.ics.githubuserskills.ranking.stats;

import java.util.Collection;

import edu.uci.ics.githubuserskills.profile.UserDomainRanking;
import edu.uci.ics.githubuserskills.profile.UserProfile;
import edu.uci.ics.githubuserskills.profile.UserRankingEntry;

/**
 * @author matias
 * 
 */
public class ProfileStatsCalculator {

	public static void calculateProfileStats(Collection<UserProfile> profiles, UserProfile aggregatedProfile) {
		aggregatedProfile.prepareForStats();

		// For each User Profile...
		for (UserProfile eachProfile : profiles) {
			calculateUserProfileStats(aggregatedProfile, eachProfile);
		}
	}

	private static void calculateUserProfileStats(UserProfile aggregatedProfile, UserProfile user) {
		user.prepareForStats();

		// Calculate User Domain Stats.
		for (UserDomainRanking eachUserProfile : user.getDomainRankings().values()) {
			calculateIntraDomainStats(aggregatedProfile.getDomainRanking(eachUserProfile.getDomain()), eachUserProfile);
		}

		calculateInterDomainStats(user);
	}

	/**
	 * Calculate inter-domain stats. Basically, the contribution of each profile
	 * to the user portfolio.
	 * 
	 * <ul>
	 * <li>N(p) = Number of terms in the profile p.</li>
	 * <li>M(u) = Number of profiles for the user u.</li>
	 * <li>f(i,p,u) = Frequency of terms i for the user u in the profile p.</li>
	 * <li>w(p,u) = Absolute weight of profile p for user u. Sum[0<i<N] f(i,p,u)</li>
	 * <li>W(u) = Total weight of all of the profiles. Sum[0<i<M] f(i,p,u)</li>
	 * <li>V(p,u) = Contribution of profile p in portfolio for user u. w(p,u) / W(u)</li>
	 * 
	 * @param user
	 *            The user to analyze.
	 */
	private static void calculateInterDomainStats(UserProfile user) {
		long totalWeightOfAllOfTheProfiles = 0L;
		for (UserDomainRanking eachProfile : user.getDomainRankings().values()) {
			totalWeightOfAllOfTheProfiles += eachProfile.getSumOfAllFreqs();
		}

		for (UserDomainRanking eachProfile : user.getDomainRankings().values()) {
			eachProfile.getDomainStats().setProfileWeight((double) eachProfile.getSumOfAllFreqs() / (double) totalWeightOfAllOfTheProfiles);
		}
	}

	/**
	 * Calculates the intra-domain stats for a given user's domain. Results are
	 * put in the userDomainRanking's domain stats object. The following
	 * formulas are relative to a fixed Domain.
	 * 
	 * <ul>
	 * <li>n = Number of terms.</li>
	 * <li>w(i) = Weight of term i.</li>
	 * <li>f(i,u) = Frequency of term i for the user u.</li>
	 * <li>score(u) = Sum[0<i<n] f(i,u)*w(i)</li>
	 * </ul>
	 * 
	 * 
	 */
	private static void calculateIntraDomainStats(UserDomainRanking aggregatedDomainRanking, UserDomainRanking userDomainRanking) {
		double domainScore = 0;
		for (UserRankingEntry eachUserRankingEntry : userDomainRanking.getTerms().values()) {
			double wi = aggregatedDomainRanking.getTerms().get(eachUserRankingEntry.getTerm()).getWeight();
			domainScore += eachUserRankingEntry.getFrequency() * wi;
		}
		userDomainRanking.getDomainStats().setScore(domainScore);
	}
}
