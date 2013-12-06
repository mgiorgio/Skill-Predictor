package edu.uci.ics.githubuserskills.indexing;

import static org.mockito.Mockito.spy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.uci.ics.githubuserskills.lucene.LuceneRAMDirectoryFactory;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;
import edu.uci.ics.githubuserskills.ranking.LuceneStoreUserRankingCreator;

@RunWith(MockitoJUnitRunner.class)
public class RawSkillDataIndexerJUnitTC {

	private LuceneStoreUserRankingCreator luceneStoredUserRankingCreator = spy(new LuceneStoreUserRankingCreator());

	public RawSkillDataIndexerJUnitTC() {
	}

	@Before
	public void setUp() {
		this.luceneStoredUserRankingCreator.setDirectoryFactory(new LuceneRAMDirectoryFactory());
		this.luceneStoredUserRankingCreator.initialize();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testThatLuceneDocumentsContainTheProperInformation() {
		RawSkillData rawData = new RawSkillData();

		final String author = "Author";
		long currentTime = System.currentTimeMillis();
		SkillDataType dataType = SkillDataType.COMMIT_PATCH;
		String contents = "Just some text";

		rawData.setAuthor(author);
		rawData.setTimestamp(currentTime);
		rawData.setType(dataType);
		rawData.setContents(contents);
	}

}
