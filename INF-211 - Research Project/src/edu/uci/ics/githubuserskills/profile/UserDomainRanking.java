package edu.uci.ics.githubuserskills.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.githubuserskills.ranking.stats.UserDomainStats;

/**
 * @author Matias
 * 
 */
public class UserDomainRanking {

	private static final Logger console = LoggerFactory.getLogger("console");

	private String domain;

	private String author;

	private Map<String, UserRankingEntry> terms;

	private long sumOfAllFreqs;

	private UserDomainStats domainStats;

	public UserDomainRanking() {
		this.terms = new HashMap<String, UserRankingEntry>();
	}

	public UserDomainStats getDomainStats() {
		if (this.domainStats == null) {
			this.domainStats = new UserDomainStats(getDomain());
		}
		return domainStats;
	}

	public void setDomainStats(UserDomainStats domainStats) {
		this.domainStats = domainStats;
	}

	public Map<String, UserRankingEntry> getTerms() {
		return new HashMap<String, UserRankingEntry>(this.terms);
	}

	public void prepareForStats() {
		this.calculateWeights();
	}

	private void calculateWeights() {
		for (UserRankingEntry entry : terms.values()) {
			entry.setWeight((double) entry.getFrequency() / (double) this.sumOfAllFreqs);
		}
	}

	public long getSumOfAllFreqs() {
		return this.sumOfAllFreqs;
	}

	/**
	 * Accumulates the frequency for this term. The total result will be
	 * accessible in {@link UserDomainRanking#sumOfAllFreqs}.
	 * 
	 * @param freqEntry
	 */
	private void accumulateTermFrequency(UserRankingEntry freqEntry) {
		this.sumOfAllFreqs += freqEntry.getFrequency();
	}

	public void addEntry(UserRankingEntry frequencyPair) {
		this.terms.put(frequencyPair.getTerm(), frequencyPair);
		this.accumulateTermFrequency(frequencyPair);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	protected void sort(List<UserRankingEntry> terms) {
		Collections.sort(terms, new Comparator<UserRankingEntry>() {

			@Override
			public int compare(UserRankingEntry o1, UserRankingEntry o2) {
				if (o1.getFrequency() > o2.getFrequency()) {
					return -1;
				} else if (o1.getFrequency() < o2.getFrequency()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	public List<UserRankingEntry> getSortedTerms() {
		List<UserRankingEntry> listOfTerms = new ArrayList<UserRankingEntry>(this.terms.values());
		this.sort(listOfTerms);
		return listOfTerms;
	}
}