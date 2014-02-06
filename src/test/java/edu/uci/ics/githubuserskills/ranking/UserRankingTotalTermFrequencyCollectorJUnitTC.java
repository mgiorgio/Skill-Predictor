package edu.uci.ics.githubuserskills.ranking;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.githubuserskills.controller.UserRankingCreationException;
import edu.uci.ics.githubuserskills.profile.ProfileTotalTermFrequencyCalculator;
import edu.uci.ics.githubuserskills.profile.SkillTermsDictionary;
import edu.uci.ics.githubuserskills.profile.UserDomainRanking;
import edu.uci.ics.githubuserskills.profile.UserProfile;
import edu.uci.ics.githubuserskills.profile.UserRankingEntry;

/**
 * @author matias
 *
 */
public class UserRankingTotalTermFrequencyCollectorJUnitTC {

	private ManualUserProfileCreator manualCreator;

	private ProfileTotalTermFrequencyCalculator totalCollector;

	@Before
	public void setUp() {
		List<String> words = Arrays.asList("term1", "term2");
		SkillTermsDictionary dictionary = new SkillTermsDictionary("java", new HashSet<String>(words));

		this.manualCreator = new ManualUserProfileCreator();
		this.totalCollector = new ProfileTotalTermFrequencyCalculator(this.getManualCreator(), Arrays.asList(dictionary));
	}

	private void feedWithUserData(String author, UserDomainRanking... rankings) throws UserRankingCreationException {
		UserProfile profile = new UserProfile(author);

		for (int i = 0; i < rankings.length; i++) {
			profile.addUserDomainRanking(rankings[i]);
		}

		this.getManualCreator().setNextResult(profile);
		totalCollector.rank(null, null);
	}

	@Test
	public void testTotalTermCollector() throws UserRankingCreationException {
		String author1 = "author1";
		UserDomainRanking ranking11 = this.createRanking("java", author1, entry("term1", 10L), entry("term2", 15L));
		UserDomainRanking ranking12 = this.createRanking("web", author1, entry("term1", 11L), entry("term2", 16L));
		feedWithUserData(author1, ranking11, ranking12);

		String author2 = "author2";
		UserDomainRanking ranking21 = this.createRanking("java", author2, entry("term1", 12L), entry("term2", 17L));
		UserDomainRanking ranking22 = this.createRanking("web", author2, entry("term1", 13L), entry("term2", 18L));
		feedWithUserData(author2, ranking21, ranking22);

		totalCollector.close();

		// Assert java profile.
		UserProfile totalProfile = totalCollector.getTotalProfile();

		UserDomainRanking javaRanking = getTotalRankingFor("java", totalProfile);

		List<UserRankingEntry> javaTerms = javaRanking.getSortedTerms();

		Assert.assertEquals("Total ranking for java:term2 is incorrect", 32, javaTerms.get(0).getFrequency());
		Assert.assertEquals("Total ranking for java:term1 is incorrect", 22, javaTerms.get(1).getFrequency());

		UserDomainRanking webRanking = getTotalRankingFor("web", totalProfile);

		List<UserRankingEntry> webTerms = webRanking.getSortedTerms();

		Assert.assertEquals("Total ranking for web:term2 is incorrect", 34, webTerms.get(0).getFrequency());
		Assert.assertEquals("Total ranking for web:term1 is incorrect", 24, webTerms.get(1).getFrequency());
	}

	private UserDomainRanking getTotalRankingFor(String profile, UserProfile totalProfile) {
		return totalProfile.getDomainRanking(profile);
	}

	private UserRankingEntry entry(String term, Long freq) {
		return new UserRankingEntry(term, freq);
	}

	private UserDomainRanking createRanking(String profile, String author, UserRankingEntry... entries) {
		UserDomainRanking ranking = new UserDomainRanking();
		ranking.setAuthor(author);
		ranking.setDomain(profile);
		for (int i = 0; i < entries.length; i++) {
			ranking.addEntry(entries[i]);
		}
		return ranking;
	}

	public ManualUserProfileCreator getManualCreator() {
		return manualCreator;
	}
}
