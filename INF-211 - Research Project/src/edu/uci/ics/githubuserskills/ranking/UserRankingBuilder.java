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

	/**
	 * 
	 */
	public UserRankingBuilder() {
		this.reset();
	}

	public void increment(String term) {
		Long freq = this.freqs.get(term);

		if (freq == null) {
			freq = 0L;
		} else {
			freq++;
		}
		this.freqs.put(term, freq);
	}

	public UserRanking build() {
		UserRanking ranking = new UserRanking();

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
