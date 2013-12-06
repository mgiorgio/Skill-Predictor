package edu.uci.ics.githubuserskills.indexing;

import java.util.List;

import edu.uci.ics.githubuserskills.model.RawSkillData;

public interface RawSkillDataIndexer {

	public void index(List<RawSkillData> rawSkillDataObjects) throws IndexingException;

	public void initialize();

	public void close() throws IndexingException;
}
