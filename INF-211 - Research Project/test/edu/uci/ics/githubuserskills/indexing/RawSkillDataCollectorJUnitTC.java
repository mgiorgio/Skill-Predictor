package edu.uci.ics.githubuserskills.indexing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.uci.ics.githubuserskills.model.RawSkillData;

@RunWith(MockitoJUnitRunner.class)
public class RawSkillDataCollectorJUnitTC {

	private RawSkillDataCollector collector = spy(new RawSkillDataCollector());

	public RawSkillDataCollectorJUnitTC() {
	}

	@Before
	public void setUp() {
		try {
			this.collector.initialize();
		} catch (IndexingException e) {
			Assert.fail(e.getMessage());
		}
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testThatDocumentsArePassedToLucene() {
		try {
			this.collector.index(mock(RawSkillData.class));
		} catch (IndexingException e) {
			Assert.fail(e.getMessage());
		}

		verify(this.collector).getIndexWriter();
	}

}
