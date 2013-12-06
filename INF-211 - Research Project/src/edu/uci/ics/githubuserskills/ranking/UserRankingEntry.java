package edu.uci.ics.githubuserskills.ranking;

public class UserRankingEntry {

	private String term;

	private long frequency;

	public UserRankingEntry() {
		this(null, 0L);
	}

	public UserRankingEntry(String term, long frequency) {
		this.setFrequency(frequency);
		this.setTerm(term);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

}
