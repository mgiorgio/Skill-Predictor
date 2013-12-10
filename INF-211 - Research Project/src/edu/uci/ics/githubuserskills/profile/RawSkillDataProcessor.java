package edu.uci.ics.githubuserskills.ranking;

import java.util.Collection;
import java.util.List;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.model.RawSkillData;

public interface UserRankingCreator {

	public Collection<UserRanking> rank(String author, List<RawSkillData> rawSkillDataObjects) throws UserRankingCreationException;

	public void initialize() throws UserRankingCreationException;

	public void close() throws UserRankingCreationException;
}
