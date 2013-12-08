/**
 * 
 */
package edu.uci.ics.githubuserskills.ranking;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Matias
 * 
 */
public class UserRankingBuilder {

	private Map<String, Long> freqs;

	private String author;

	private String profile;

	/**
	 * 
	 */
	public UserRankingBuilder() {
		this.reset();
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void increment(String term) {
		Long freq = this.freqs.get(term);

		if (freq == null) {
			freq = 1L;
		} else {
			freq++;
		}
		this.freqs.put(term, freq);
	}

	public UserRanking build() {
		UserRanking ranking = new UserRanking();

		ranking.setAuthor(author);
		ranking.setProfile(profile);

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
