package edu.uci.ics.githubuserskills.ranking.stats;

import java.util.Collection;

import edu.uci.ics.githubuserskills.profile.UserDomainRanking;
import edu.uci.ics.githubuserskills.profile.UserProfile;
import edu.uci.ics.githubuserskills.profile.UserRankingEntry;

public class IntraDomainStatsCalculator {

	public static void calculateIntraDomainStats(Collection<UserProfile> profiles, UserProfile aggregatedProfile) {
		aggregatedProfile.prepareForStats();

		// For each User Profile...
		for (UserProfile eachProfile : profiles) {
			calculateUserProfileIntraDomainStats(aggregatedProfile, eachProfile);
		}
	}

	private static void calculateUserProfileIntraDomainStats(UserProfile aggregatedProfile, UserProfile eachProfile) {
		eachProfile.prepareForStats();

		// Calculate User Domain Stats.
		for (UserDomainRanking eachUserDomainRanking : eachProfile.getDomainRankings().values()) {
			calculateUserDomainExpertise(aggregatedProfile.getDomainRanking(eachUserDomainRanking.getDomain()), eachUserDomainRanking);
		}
	}

	private static void calculateUserDomainExpertise(UserDomainRanking aggregatedDomainRanking, UserDomainRanking userDomainRanking) {
		/*
		 * The following formulas are relative to a fixed Domain.
		 * 
		 * n = Number of terms. w(i) = Weight of term i. f(i,u) = Frequency of
		 * term i for the user u.
		 * 
		 * expertise(u) = Sum[0<i<n] f(i,u)*w(i)
		 */
		double domainExpertise = 0;
		for (UserRankingEntry eachUserRankingEntry : userDomainRanking.getTerms().values()) {
			double wi = aggregatedDomainRanking.getTerms().get(eachUserRankingEntry.getTerm()).getWeight();
			domainExpertise += eachUserRankingEntry.getFrequency() * wi;
		}
		userDomainRanking.getDomainStats().setExpertise(domainExpertise);
	}
}
