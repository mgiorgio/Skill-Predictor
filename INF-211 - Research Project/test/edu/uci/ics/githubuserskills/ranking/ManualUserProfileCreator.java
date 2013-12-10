package edu.uci.ics.githubuserskills.ranking;

import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.profile.RawSkillDataProcessor;
import edu.uci.ics.githubuserskills.profile.UserProfile;

/**
 * @author matias
 * 
 */
public class ManualUserProfileCreator implements RawSkillDataProcessor {

	private UserProfile nextResult;

	public ManualUserProfileCreator() {
	}

	@Override
	public UserProfile rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException {
		return this.getNextResult();
	}

	public void setNextResult(UserProfile nextRankResult) {
		this.nextResult = nextRankResult;
	}

	public UserProfile getNextResult() {
		return nextResult;
	}

	@Override
	public void initialize() throws UserRankingCreationException {
		// Do nothing.
	}

	@Override
	public void close() throws UserRankingCreationException {
		// Do nothing.
	}
}