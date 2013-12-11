package edu.uci.ics.githubuserskills.profile;

import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * @author matias
 *
 */
public class UserProfileCreatorDecorator implements RawSkillDataProcessor {

	private RawSkillDataProcessor innerCreator;

	public UserProfileCreatorDecorator(RawSkillDataProcessor innerCreator) {
		this.innerCreator = innerCreator;
	}

	@Override
	public UserProfile rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		return innerCreator.rank(author, rawSkillDataObjects);
	}

	@Override
	public void initialize() throws UserRankingCreationException {
		innerCreator.initialize();
	}

	@Override
	public void close() throws UserRankingCreationException {
		innerCreator.close();
	}

	protected RawSkillDataProcessor getInnerRankingCreator() {
		return this.innerCreator;
	}

}
