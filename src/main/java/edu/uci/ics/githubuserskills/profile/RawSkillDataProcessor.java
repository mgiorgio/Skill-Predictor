package edu.uci.ics.githubuserskills.profile;

import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

/**
 * @author matias
 *
 */
public interface RawSkillDataProcessor {

	public UserProfile rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException;

	public void initialize() throws UserRankingCreationException;

	public void close() throws UserRankingCreationException;
}
