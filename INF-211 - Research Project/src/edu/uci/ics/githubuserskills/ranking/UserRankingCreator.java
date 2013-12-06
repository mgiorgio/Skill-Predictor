package edu.uci.ics.githubuserskills.ranking;

import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

public interface UserRankingCreator {

	public UserRanking rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException;

	public void initialize();

	public void close() throws UserRankingCreationException;
}
