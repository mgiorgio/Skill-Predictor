package edu.uci.ics.githubuserskills.profile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author matias
 *
 */
public class UserProfileBuilder extends UserDomainRankingBuilder {

	private Map<SkillTermsDictionary, UserDomainRankingBuilder> builders;

	public UserProfileBuilder(Collection<SkillTermsDictionary> dictionaries) {
		this.initBuilders(dictionaries);
	}

	private void initBuilders(Collection<SkillTermsDictionary> dictionaries) {
		builders = new HashMap<SkillTermsDictionary, UserDomainRankingBuilder>();
		for (SkillTermsDictionary skillTermsDictionary : dictionaries) {
			UserDomainRankingBuilder rankingBuilder = new UserDomainRankingBuilder();
			rankingBuilder.setProfile(skillTermsDictionary.getName());
			builders.put(skillTermsDictionary, rankingBuilder);
		}
	}

	public void increment(String term, long quantity) {
		for (Entry<SkillTermsDictionary, UserDomainRankingBuilder> entry : builders.entrySet()) {
			if (this.isInDictionary(term, entry.getKey())) {
				entry.getValue().increment(term, quantity);
			}
		}
	}

	private boolean isInDictionary(String term, SkillTermsDictionary dictionary) {
		return dictionary.getDictionary().contains(term);
	}

	public UserProfile buildProfile() {
		UserProfile profile = new UserProfile(getAuthor());

		for (UserDomainRankingBuilder eachBuilder : builders.values()) {
			eachBuilder.setAuthor(this.getAuthor());
			UserDomainRanking userDomainRanking = eachBuilder.build();
			profile.addUserDomainRanking(userDomainRanking);
		}

		return profile;
	}
}
