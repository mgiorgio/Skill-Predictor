/**
 * 
 */
package edu.uci.ics.githubuserskills.profile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * @author matias
 * 
 */
public class ProfileTotalTermFrequencyCalculator extends UserProfileCreatorDecorator {

	private static final String TOTAL = "TOTAL";

	private Map<String, UserDomainRankingBuilder> rankingBuilders;

	private UserProfile totalProfile;

	public ProfileTotalTermFrequencyCalculator(RawSkillDataProcessor rankingCreator, Collection<SkillTermsDictionary> dictionaries) {
		super(rankingCreator);
		this.rankingBuilders = new HashMap<String, UserDomainRankingBuilder>();
		this.totalProfile = new UserProfile(TOTAL);
	}

	@Override
	public UserProfile rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		UserProfile profile = super.rank(author, rawSkillDataObjects);

		for (UserDomainRanking userRanking : profile.getDomainRankings().values()) {
			for (UserRankingEntry eachTerm : userRanking.getTerms().values()) {
				UserDomainRankingBuilder rankingBuilder = this.getRankingBuilderFor(userRanking.getDomain());
				rankingBuilder.increment(eachTerm.getTerm(), eachTerm.getFrequency());
			}
		}

		return profile;
	}

	private UserDomainRankingBuilder getRankingBuilderFor(String profile) {
		UserDomainRankingBuilder rankingBuilder = this.rankingBuilders.get(profile);

		if (rankingBuilder == null) {
			rankingBuilder = new UserDomainRankingBuilder();
			rankingBuilder.setAuthor(ProfileTotalTermFrequencyCalculator.TOTAL);
			rankingBuilder.setProfile(profile);
			this.rankingBuilders.put(profile, rankingBuilder);
		}

		return rankingBuilder;
	}

	@Override
	public void close() throws UserRankingCreationException {
		for (Entry<String, UserDomainRankingBuilder> eachEntry : rankingBuilders.entrySet()) {
			this.totalProfile.addUserDomainRanking(eachEntry.getValue().build());
		}
	}

	public UserProfile getTotalProfile() {
		return this.totalProfile;
	}
}
