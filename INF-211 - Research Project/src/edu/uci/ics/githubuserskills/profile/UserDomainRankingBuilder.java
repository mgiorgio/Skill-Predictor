/**
 * 
 */
package edu.uci.ics.githubuserskills.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Matias
 * 
 */
public class UserDomainRankingBuilder {

	private Map<String, Long> freqs;

	private String author;

	private String profile;

	/**
	 * 
	 */
	public UserDomainRankingBuilder() {
		this.reset();
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getAuthor() {
		return author;
	}

	public String getProfile() {
		return profile;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void increment(String term, long quantity) {
		Long freq = this.freqs.get(term);

		if (freq == null) {
			freq = quantity;
		} else {
			freq += quantity;
		}
		this.freqs.put(term, freq);
	}

	public void increment(String term) {
		this.increment(term, 1L);
	}

	public UserDomainRanking build() {
		UserDomainRanking ranking = new UserDomainRanking();

		ranking.setAuthor(author);
		ranking.setDomain(profile);

		for (Entry<String, Long> freq : this.freqs.entrySet()) {
			ranking.addEntry(new UserRankingEntry(freq.getKey(), freq.getValue()));
		}

		this.reset();

		return ranking;
	}

	private void reset() {
		this.freqs = new HashMap<String, Long>();
	}

}
