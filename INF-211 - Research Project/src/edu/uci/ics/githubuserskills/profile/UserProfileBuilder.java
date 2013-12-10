package edu.uci.ics.githubuserskills.ranking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CompoundUserRankingBuilder extends UserRankingBuilder {

	private Map<SkillTermsDictionary, UserRankingBuilder> builders;

	public CompoundUserRankingBuilder(Collection<SkillTermsDictionary> dictionaries) {
		this.initBuilders(dictionaries);
	}

	private void initBuilders(Collection<SkillTermsDictionary> dictionaries) {
		builders = new HashMap<SkillTermsDictionary, UserRankingBuilder>();
		for (SkillTermsDictionary skillTermsDictionary : dictionaries) {
			UserRankingBuilder rankingBuilder = new UserRankingBuilder();
			rankingBuilder.setProfile(skillTermsDictionary.getName());
			builders.put(skillTermsDictionary, rankingBuilder);
		}
	}

	@Override
	public void setAuthor(String author) {
		for (UserRankingBuilder eachBuilder : builders.values()) {
			eachBuilder.setAuthor(author);
		}
	}

	@Override
	public void increment(String term) {
		for (Entry<SkillTermsDictionary, UserRankingBuilder> entry : builders.entrySet()) {
			if (this.isInDictionary(term, entry.getKey())) {
				entry.getValue().increment(term);
			}
		}
	}

	private boolean isInDictionary(String term, SkillTermsDictionary dictionary) {
		return dictionary.getDictionary().contains(term);
	}

	public Collection<UserRanking> buildAll() {
		Collection<UserRanking> rankings = new ArrayList<UserRanking>();

		for (UserRankingBuilder eachBuilder : builders.values()) {
			rankings.add(eachBuilder.build());
		}

		return rankings;
	}
}
