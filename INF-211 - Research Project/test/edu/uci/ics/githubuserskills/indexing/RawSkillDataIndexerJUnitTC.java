package edu.uci.ics.githubuserskills.indexing;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.uci.ics.githubuserskills.lucene.LuceneRAMDirectoryFactory;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;

@RunWith(MockitoJUnitRunner.class)
public class RawSkillDataIndexerJUnitTC {

	private RawSkillDataIndexer indexer = spy(new RawSkillDataIndexer());

	public RawSkillDataIndexerJUnitTC() {
	}

	@Before
	public void setUp() {
		this.indexer.initialize();
		this.indexer.setDirectoryFactory(new LuceneRAMDirectoryFactory());
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

	@Test
	public void testThatADifferentIndexWriterIsUsedForEachUser() {
		final String user1 = "user1";
		final String user2 = "user2";
		final RawSkillData data1 = createTestRawDataForUser(user1);
		final RawSkillData data2 = createTestRawDataForUser(user2);
		verifyIndexMethodUseTheProperIndexWriter(user1, Arrays.asList(data1));
		verifyIndexMethodUseTheProperIndexWriter(user2, Arrays.asList(data2));

	}

	private void verifyIndexMethodUseTheProperIndexWriter(String user1, List<RawSkillData> rawDatasForUser1) {
		try {
			this.indexer.index(rawDatasForUser1);
			verify(this.indexer).getIndexWriterForUser(user1);
		} catch (IndexingException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	private RawSkillData createTestRawDataForUser(String user) {
		RawSkillData data1 = new RawSkillData();
		data1.setAuthor(user);
		return data1;
	}

}
