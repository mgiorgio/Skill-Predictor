/**
 * 
 */
package edu.uci.ics.githubuserskills.indexing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matias
 * 
 */
public class FreqCalculator {

	private Map<String, Long> freqs;

	/**
	 * 
	 */
	public FreqCalculator() {
		this.freqs = new HashMap<String, Long>();
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

	public Map<String, Long> freqs() {
		return new HashMap<String, Long>(this.freqs);
	}

}
